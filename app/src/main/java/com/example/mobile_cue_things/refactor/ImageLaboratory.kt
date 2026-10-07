package com.example.mobile_cue_things.refactor

// Imports
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import kotlin.math.max
import kotlin.math.roundToInt

suspend fun saveUriToExternalPicturesAsSquareJpegWithSampling(
    context: Context,
    sourceUri: Uri,
    email: String,
    filename: String? = null,
    size: Int = 250,
    compressQuality: Int = 90
): File? = withContext(Dispatchers.IO) {
    try {
        // 1) Sanear email para nombre de carpeta
        val safeEmail = email.replace(Regex("[\\\\/:*?\"<>|]"), "_")

        // 2) Crear carpeta: /sdcard/Android/data/<package>/files/Pictures/<safeEmail>/
        val picturesDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (picturesDir == null) return@withContext null
        val userDir = File(picturesDir, safeEmail)
        if (!userDir.exists()) {
            userDir.mkdirs()
        }

        // 3) Generar filename si no fue dado (asegurarse .jpg)
        val outName = (filename ?: "img_${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg")
            .let { if (it.endsWith(".jpg", true) || it.endsWith(".jpeg", true)) it else "$it.jpg" }
        val outFile = File(userDir, outName)

        // 4) Decodificar la Uri a Bitmap usando sampling/targetSize según API
        // Queremos decodificar a un tamaño suficiente para luego crop+scale a `size`
        // Calculamos decodeTarget = size * 2 como heurística para mantener calidad al recortar.
        val decodeTarget = size * 2

        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ImageDecoder: pedimos target size para ahorrar memoria
            val src = ImageDecoder.createSource(context.contentResolver, sourceUri)
            ImageDecoder.decodeBitmap(src) { decoder, info, _ ->
                // Pedimos un tamaño aproximado. Si la imagen original es enorme, esto reduce memoria.
                decoder.setTargetSize(decodeTarget, decodeTarget)
                // opcional: decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            // API antiguas: usar inSampleSize
            // 1) Leer dimensiones sin cargar bitmap
            val optionsBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(sourceUri).use { streamBounds: InputStream? ->
                BitmapFactory.decodeStream(streamBounds, null, optionsBounds)
            }

            val reqWidth = decodeTarget
            val reqHeight = decodeTarget
            val inSampleSize = calculateInSampleSize(optionsBounds, reqWidth, reqHeight)

            // 2) Decodificar con inSampleSize
            val options = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inJustDecodeBounds = false
            }

            context.contentResolver.openInputStream(sourceUri).use { streamDecode: InputStream? ->
                val bmp = BitmapFactory.decodeStream(streamDecode, null, options)
                bmp ?: throw IllegalArgumentException("No se pudo decodificar la imagen desde la Uri")
            }
        }

        // 5) Center-crop + scale a 'size' x 'size'
        val squareBitmap = centerCropAndScale(bitmap, size, size)

        // 6) Escribir JPEG al archivo de salida
        FileOutputStream(outFile).use { fos ->
            squareBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fos)
            fos.flush()
        }

        // 7) Liberar bitmaps
        if (squareBitmap !== bitmap) {
            bitmap.recycle()
        }
        squareBitmap.recycle()

        return@withContext outFile
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext null
    }
}

/**
 * Calcula inSampleSize (estándar) para aproximar la decodificación al tamaño requerido.
 */
private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calcula el mayor inSampleSize que sea potencia de 2 y mantenga height/width mayores que req
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * Realiza center-crop y escala al tamaño deseado.
 * Mantiene la mayor parte central de la imagen.
 */
private fun centerCropAndScale(source: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
    // Primero, escalar manteniendo ratio para cubrir el target (scale up/down)
    val srcWidth = source.width
    val srcHeight = source.height

    if (srcWidth == targetWidth && srcHeight == targetHeight) {
        return source.copy(source.config ?: Bitmap.Config.ARGB_8888, true)
    }

    // Escalar para cubrir (cover)
    val scale = max(
        targetWidth.toFloat() / srcWidth.toFloat(),
        targetHeight.toFloat() / srcHeight.toFloat()
    )

    val scaledWidth = (scale * srcWidth).roundToInt()
    val scaledHeight = (scale * srcHeight).roundToInt()

    val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)

    // Calcular offsets para crop central
    val left = (scaledWidth - targetWidth) / 2
    val top = (scaledHeight - targetHeight) / 2

    val cropped = Bitmap.createBitmap(scaled, left, top, targetWidth, targetHeight)

    // Liberar scaled si es distinto del source
    if (scaled != source && !scaled.isRecycled) {
        scaled.recycle()
    }

    return cropped
}


