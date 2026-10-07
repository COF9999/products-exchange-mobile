package com.example.mobile_cue_things.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName


data class Offer(
    @DocumentId
    val id: String = "",

    @JvmField
    @PropertyName("email_offerer")
    val emailOfferer: String = "",

    @JvmField
    @PropertyName("id_product")
    val idProduct: String = "",

    @JvmField
    @PropertyName("price_to_offer")
    val priceToOffer: Float = 0f,

    @JvmField
    @PropertyName("product_to_offer")
    val productToOffer: String = "",

    @JvmField
    @PropertyName("status_offer")
    val statusOffer: Int = 1
) {

}