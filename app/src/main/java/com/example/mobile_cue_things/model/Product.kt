package com.example.mobile_cue_things.model

import com.google.firebase.firestore.DocumentId


data class Product(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imgUrl: String = "",
    val emailUser: String = "",
    val category: String = "",
    val condition: Int = 0,          // 1 a 10
    val createdAt: String = "",      // formato: "2025-11-30"
    val brand: String = ""
){
    override fun toString(): String {
        return "Product(id='$id', name='$name', price=$price, description='$description', imgUrl='$imgUrl')"
    }
}