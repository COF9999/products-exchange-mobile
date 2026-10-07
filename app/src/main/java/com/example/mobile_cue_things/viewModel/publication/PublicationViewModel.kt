package com.example.mobile_cue_things.viewModel.publication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PublicationViewModel: ViewModel() {

    var idProduct by mutableStateOf("")

    var ownerProduct by mutableStateOf("")


    fun updateIdProduct(newIdProduct:String){
        idProduct = newIdProduct
    }


    fun updateOwnerProduct(newOwnerProduct:String){
        ownerProduct = newOwnerProduct
    }


}