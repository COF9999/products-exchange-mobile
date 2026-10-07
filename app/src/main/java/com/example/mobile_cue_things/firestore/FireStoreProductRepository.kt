package com.example.mobile_cue_things.firestore

import com.example.mobile_cue_things.enums.ConstansDatabase
import com.example.mobile_cue_things.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreProductRepository {
    private val db = Firebase.firestore

    fun uploadProduct(
        product: Product,
        onSuccess: (String)-> Unit,
        onFailure:(Exception)-> Unit
    ){
        db.collection(ConstansDatabase.PRODUCTS.value)
            .add(product)
            .addOnSuccessListener { ref-> onSuccess(ref.id) }
            .addOnFailureListener { e-> onFailure(e)}
    }

    suspend fun getProductById(id: String): Product? =
        suspendCoroutine { continuation ->
            db.collection(ConstansDatabase.PRODUCTS.value)
                .document(id)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        continuation.resume(doc.toObject(Product::class.java))
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }

    suspend fun getAllProducts(): List<Product> {
        val snapshot = db.collection("products").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
    }
}