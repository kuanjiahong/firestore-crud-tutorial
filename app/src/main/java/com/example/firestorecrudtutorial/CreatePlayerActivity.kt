package com.example.firestorecrudtutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreatePlayerActivity : AppCompatActivity() {

    private val tag = "Create Activity"
    private lateinit var createButton: Button
    private lateinit var nameTextField : EditText
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_player)

        createButton = findViewById(R.id.create_button)
        nameTextField = findViewById(R.id.player_name_input_field)

        createButton.setOnClickListener {
            val playerName : String = nameTextField.text!!.toString()
            createPlayer(playerName)
            createMonster()
            goToRUDActivity()

        }

    }

    private fun goToRUDActivity() {
        val intent = Intent(this, ReadOrUpdateActivity::class.java)
        intent.putExtra("player_created", "created")
        Log.d(tag, "Intent created")
        startActivity(intent)
        finish()
    }

    private fun createPlayer(playerName : String) {
        val player = Player(playerName, 100)
        db.collection("players")
            .document("first-player")
            .set(player)
            .addOnSuccessListener {
                Log.d(tag, "Player created success!")
            }.addOnFailureListener{ e ->
                Log.w(tag, "Fail to create player", e)
            }
    }

    private fun createMonster() {
        val monster = Monster("Godzilla", 100)
        db.collection("monsters")
            .document("first-monster").set(monster).addOnSuccessListener {
                Log.d(tag, "Monster created success!")
            }.addOnFailureListener{ e ->
                Log.w(tag, "Fail to create monster", e)
            }
    }

}