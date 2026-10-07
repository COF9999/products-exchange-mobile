package com.example.mobile_cue_things.viewModel.global

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GlobalVariables: ViewModel() {
    var userName by mutableStateOf("")
        private set

    var statePanel by mutableStateOf(false)

    var positionRoute by mutableStateOf("")

    fun changeStatePanel(){
        statePanel = !statePanel
    }

    fun updateUserName(name:String){
        userName = name
    }

    fun updatePositionRoute(newPositionRoute:String){
        positionRoute = newPositionRoute
    }

    fun clearUserName(){
        userName = ""
    }
}