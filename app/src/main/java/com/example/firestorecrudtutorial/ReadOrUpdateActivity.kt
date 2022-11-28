package com.example.firestorecrudtutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReadOrUpdateActivity : AppCompatActivity() {
    private val tag = "RUD Activity"
    private lateinit var monsterLabel : TextView
    private lateinit var monsterName : TextView
    private lateinit var monsterHealth : TextView
    private lateinit var monsterHealthLabel : TextView

    private lateinit var playerLabel : TextView
    private lateinit var playerName : TextView

    private lateinit var announcementText : TextView
    private lateinit var attackButton : Button
    private lateinit var restartButton : Button
    private val db = Firebase.firestore

    private lateinit var monsterProfile : Monster

    private val monsterViewModel : MonsterViewModel by viewModels()
    private val playerViewModel : PlayerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_or_update)
        Toast.makeText(this, "Player created!", Toast.LENGTH_SHORT).show()
        monsterLabel = findViewById(R.id.monster_label)
        monsterName = findViewById(R.id.monster_name)
        monsterHealth = findViewById(R.id.monster_health)
        monsterHealthLabel = findViewById(R.id.monster_health_label)

        monsterProfile = Monster()

        monsterName.text = ""
        monsterHealth.text = ""

        playerLabel = findViewById(R.id.player_label)

        playerName = findViewById(R.id.player_name)

        playerName.text = ""

        announcementText = findViewById(R.id.announcement)
        announcementText.text = ""

        attackButton = findViewById(R.id.attack_button)
        restartButton = findViewById(R.id.restart_button)

        restartButton.isClickable = false
        restartButton.isEnabled = false

        // Create the observer which updates the UI.
        val monsterObserver = Observer<Monster> { theMonster ->
            if (theMonster.health == 0) {
                Log.d(tag, "Monster health is zero")
                attackButton.isClickable = false
                attackButton.isEnabled = false
                restartButton.isEnabled = true
                restartButton.isClickable = true
                announcementText.text = getString(R.string.congratulations)
            }

            monsterName.text = theMonster.name.toString()
            monsterHealth.text = theMonster.health.toString()
        }

        val playerObserver = Observer<Player> { thePlayer ->
            playerName.text = thePlayer.name
        }

        monsterViewModel.currentMonster.observe(this, monsterObserver)
        playerViewModel.currentPlayer.observe(this, playerObserver)


    }

    override fun onStart() {
        super.onStart()

        db.collection("monsters")
            .document("first-monster")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(tag, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    monsterViewModel
                        .currentMonster
                        .value = snapshot.toObject<Monster>()
                    Log.d(tag, "Current data: ${snapshot.data}")
                } else {
                    Log.d(tag, "Current data: null")
                }
            }

        db.collection("players")
            .document("first-player")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(tag, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    playerViewModel
                        .currentPlayer
                        .value = snapshot.toObject<Player>()
                    Log.d(tag, "Current data: ${snapshot.data}")
                } else {
                    Log.d(tag, "Current data: null")
                }
            }

        attackButton.setOnClickListener{
            val healthPointsToReduce = 50
            updateMonsterHealth(monsterHealth.text.toString().toInt(), healthPointsToReduce)
        }

        restartButton.setOnClickListener {
            goToMainActivity()
        }


    }

    private fun updateMonsterHealth(
        currentHealthPoints : Int,
        healthPointsToReduce : Int)
    {
        val reducedHealth = currentHealthPoints - healthPointsToReduce
        db.collection("monsters")
            .document("first-monster")
            .update("health", reducedHealth)
            .addOnSuccessListener {
                if (reducedHealth != 0) {
                    Toast
                        .makeText(this,
                            getString(R.string.hit_message),
                            Toast.LENGTH_SHORT)
                        .show()
                }
                Log.d(tag, "Health updated success!")
            }.addOnFailureListener{ e ->
                Log.w(tag, "Error in updating health", e)
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}