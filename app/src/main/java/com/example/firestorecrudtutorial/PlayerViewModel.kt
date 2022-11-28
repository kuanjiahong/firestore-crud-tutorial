package com.example.firestorecrudtutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>()
    }

}