package com.example.firestorecrudtutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonsterViewModel : ViewModel() {

    val currentMonster: MutableLiveData<Monster> by lazy {
        MutableLiveData<Monster>()
    }

}