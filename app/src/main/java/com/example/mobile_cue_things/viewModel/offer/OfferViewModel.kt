package com.example.mobile_cue_things.viewModel.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_cue_things.enums.ConstantsManageOffer
import com.example.mobile_cue_things.firestore.FireStoreOfferRepository
import com.example.mobile_cue_things.model.Offer
import com.example.mobile_cue_things.objects.OfferWithProduct
import kotlinx.coroutines.launch


class OfferViewModel : ViewModel() {

    private val repository = FireStoreOfferRepository()


    fun createOfferCaller(
        ownerProduct: String,
        emailOffer: String,
        idProduct: String,
        priceToOffer: Float,
        productToOffer: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            if (ownerProduct==emailOffer){
                onResult(false)
                return@launch
            }

            val exists = repository.existsOfferByEmailAndId(emailOffer, idProduct,
                ConstantsManageOffer.ACTIVE.value)

            if (exists) {
                onResult(false)
                return@launch
            }

            val created = repository.createOffer(
                Offer(
                    emailOfferer = emailOffer,
                    idProduct = idProduct,
                    priceToOffer = priceToOffer,
                    productToOffer = productToOffer,
                    statusOffer = 1
                )
            )

            onResult(created)
        }
    }



    fun updateOfferStatus(offerId: String, newStatus: Int,onResult:(Boolean)-> Unit) {

        viewModelScope.launch {

            try {
                repository.updateOfferStatus(offerId, newStatus)
                onResult(true)
            } catch (t: Throwable) {
                onResult(false)
            }
        }
    }


    suspend fun getOffersByProduct(idProduct: String, statusOffer: Int = ConstantsManageOffer.ACTIVE.value): List<Offer> {
        return repository.getOffersByProduct(idProduct,statusOffer)
    }

    suspend fun getOffersByEmailUser(emailUser:String):List<Offer>{
        return repository.getOffersByEmail(emailOfferer= emailUser)
    }

    /*
    fun getOfferWithProductName(
        offerId: String,
        onResult: (OfferWithProductName?) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.getOfferWithProductName(offerId)
            onResult(result)
        }
    }


     */


    fun getOffersAndProductName(
        emailOfferer: String,
        onResult: (List<OfferWithProduct>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.getOfferWithProductName(emailOfferer)
                onResult(result)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


}
