package com.example.firestorecrudtutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val tag = "Main Activity"
    private lateinit var playButton: Button
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetDatabase()
        playButton = findViewById(R.id.play_button)
        playButton.setOnClickListener {
            goToCreateActivity()
        }


    }

    private fun goToCreateActivity() {
        val intent = Intent(this, CreatePlayerActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun resetDatabase() {
        db.collection("players")
            .document("first-player")
            .delete()
            .addOnSuccessListener {
                Log.d(tag, "first-player document deleted")
            }
            .addOnFailureListener{ e ->
                Log.w(tag, "Error deleting first-player document", e)
            }
        db.collection("monsters")
            .document("first-monster")
            .delete()
            .addOnSuccessListener {
                Log.d(tag, "first-monster documents deleted")
            }.addOnFailureListener{ e ->
                Log.w(tag, "Error deleting first-player document", e)
            }
    }
}