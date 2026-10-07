package com.example.mobile_cue_things.objects

import com.example.mobile_cue_things.model.Offer
import com.example.mobile_cue_things.model.Product

data class OfferWithProduct(
    val offer: Offer,
    val product: Product?
)
