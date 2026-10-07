package com.example.mobile_cue_things.firestore

import com.example.mobile_cue_things.enums.ConstansDatabase
import com.example.mobile_cue_things.model.Offer
import com.example.mobile_cue_things.model.Product
import com.example.mobile_cue_things.objects.OfferWithProduct
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldPath


class FireStoreOfferRepository {

    private val db = Firebase.firestore
    private val collection = ConstansDatabase.OFFERS.value

    suspend fun createOffer(offer: Offer): Boolean {
        return try {
            db.collection(collection)
                .add(offer)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateOfferStatus(offerId: String, newStatus: Int) {
        db.collection(collection)
            .document(offerId)
            .update("status_offer", newStatus)
            .await() // -> si falla lanzará excepción
    }

    fun updateOfferStatus(
        offerId: String,
        newStatus: Byte,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .document(offerId)
            .update("status_offer", newStatus)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    suspend fun getOffersByEmail(
        emailOfferer: String
    ): List<Offer> {
        return try {
            val snap = db.collection(collection)
                .whereEqualTo("email_offerer",emailOfferer)
                .get()
                .await()

            snap.toObjects(Offer::class.java)
        }catch (e:Exception){
            emptyList()
        }

    }

    suspend fun existsOfferByEmailAndId(emailOffer: String,idProduct:String,statusOffer: Int,): Boolean {
        return try {
            val querySnapshot = db.collection(collection)
                .whereEqualTo("email_offerer", emailOffer)
                .whereEqualTo("id_product", idProduct)
                .whereEqualTo("status_offer", statusOffer)
                .get()
                .await()

            !querySnapshot.isEmpty  // true si ya existe una oferta
        } catch (e: Exception) {
            false
        }
    }


    suspend fun existsOfferByEmail(email: String): Boolean {
        return try {
            val snap = db.collection(collection)
                .whereEqualTo("email_offerer", email)
                .get()
                .await()

            !snap.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getOffersByProduct(idProduct: String, statusOffer: Int ): List<Offer> {
        return try {
            val snap = db.collection(collection)
                .whereEqualTo("id_product", idProduct)
                .whereEqualTo("status_offer",statusOffer)
                .get()
                .await()

            snap.toObjects(Offer::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getOfferWithProductName(emailUser: String): List<OfferWithProduct> {
        // 1) Traer las ofertas del usuario
        val offersSnapshot = db.collection(collection)
            .whereEqualTo("email_offerer", emailUser)
            .get()
            .await()

        val offers = offersSnapshot.documents.mapNotNull { it.toObject(Offer::class.java) }

        if (offers.isEmpty()) return emptyList()

        // 2) Extraer productIds únicos
        val productIds = offers.map { it.idProduct }.filter { it.isNotBlank() }.distinct()

        // 3) Si no hay productIds, devolvemos las offers con "producto no encontrado"
        if (productIds.isEmpty()) {
            return offers.map { OfferWithProduct(it, Product())}
        }

        // 4) Traer productos en batches usando whereIn (límite ~10 por query)
        val productsById = mutableMapOf<String, Product>()
        val batchSize = 10
        val chunks = productIds.chunked(batchSize)

        for (chunk in chunks) {
            val qSnapshot = db.collection(ConstansDatabase.PRODUCTS.value)
                .whereIn(FieldPath.documentId(), chunk) // trae por id del doc
                .get()
                .await()

            qSnapshot.documents.mapNotNull { it.toObject(Product::class.java) }.forEach { prod ->
                productsById[prod.id] = prod
            }
        }

        // 5) Mapear offers -> OfferWithProductName
        return offers.map { offer ->


            val product: Product? = productsById[offer.idProduct]
            OfferWithProduct(offer = offer, product = product)
        }
    }

    }


