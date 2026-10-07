package com.example.mobile_cue_things.viewModel.product

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_cue_things.firestore.FirestoreProductRepository


import com.example.mobile_cue_things.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.File

class FirestoreProductViewModel(
) : ViewModel() {

    private val repository = FirestoreProductRepository()
    private val gson = Gson()

    fun uploadJsonFromAssets(context: Context, fileName: String) {

        val jsonString = getJsonFromAssets(context, fileName)
        if (jsonString == null) {
            Log.e("FirestoreUpload", "Error leyendo JSON")
            return
        }

        val listType = object : TypeToken<List<Product>>() {}.type
        val products: List<Product> = gson.fromJson(jsonString, listType)

        products.forEach { product ->
            repository.uploadProduct(
                product,
                onSuccess = { id ->
                    Log.d("FirestoreUpload", "Documento subido con ID: $id")
                },
                onFailure = { e ->
                    Log.e("FirestoreUpload", "Error subiendo producto", e)
                }
            )
        }
    }

    suspend fun loadProductById(id: String):Product? {
        return repository.getProductById(id)
    }

    private fun getJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    suspend fun exportProductsToJson(context: Context): String {
        return try {
            val products = repository.getAllProducts() // <-- la añadimos al repo
            val jsonString = gson.toJson(products)

            // Guardar en Descargas del dispositivo
            val fileName = "products_export.json"
            val file = File(context.getExternalFilesDir(null), fileName)

            file.writeText(jsonString)

            Log.d("FirestoreExport", "Archivo exportado en: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e("FirestoreExport", "Error exportando productos", e)
            "Error: ${e.message}"
        }
    }
}
