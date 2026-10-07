package com.example.mobile_cue_things.viewModel.product

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CreateProductViewModel() : ViewModel() {

    private val _state = MutableLiveData<UploadState>(UploadState.Idle)
    val state: LiveData<UploadState> = _state

    fun uploadProduct(
        name: String,
        description: String,
        price: Float,
        email: String,
        selectedImageUri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {
            _state.value = UploadState.Uploading

            try {
                var relativePath: String? = null

                if (selectedImageUri != null) {
                    relativePath = saveImageToPublicPictures(
                        email = email,
                        sourceUri = selectedImageUri,
                        context = context
                    )

                    if (relativePath == null) {
                        _state.value = UploadState.Failed("No se pudo guardar la imagen")
                        return@launch
                    }
                }

                val saved = saveProductToFirestore(
                    name,
                    description,
                    price,
                    relativePath // ahora guardamos la ruta local
                )

                if (!saved) {
                    _state.value = UploadState.Failed("Error guardando producto en Firestore")
                    return@launch
                }

                _state.value = UploadState.Success("Producto creado correctamente")

            } catch (e: Exception) {
                _state.value = UploadState.Failed(e.message ?: "Error desconocido")
            }
        }
    }



    suspend fun saveImageToPublicPictures(
        context: Context,
        email: String,
        sourceUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        try {
            // Decodificar la Uri a Bitmap
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return@withContext null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()


            // Crear nombre de archivo único
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val relativePath = "Pictures/${email}/$fileName"

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Carpeta en /Pictures/email
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$email")
                }
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val itemUri = resolver.insert(collection, values) ?: return@withContext null


// Validar que bitmap se haya decodificado correctamente
            bitmap?.let { bmp ->
                resolver.openOutputStream(itemUri)?.use { outStream ->
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream)
                    outStream.flush()
                }
            } ?: return@withContext null // si no se pudo decodificar, salir


            // Retornar la ruta relativa para Firestore
            relativePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    private suspend fun saveProductToFirestore(
        name: String,
        description: String,
        price: Float,
        relativePath: String?
    ): Boolean {
        val data = hashMapOf(
            "name" to name,
            "description" to description,
            "price" to price,
            "imgUrl" to relativePath
        )

        return try {
            Firebase.firestore.collection("products").add(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
    // Estados para UI
    sealed class UploadState {
        object Idle : UploadState()
        object Uploading : UploadState()
        data class Success(val message: String) : UploadState()
        data class Failed(val message: String) : UploadState()
    }

