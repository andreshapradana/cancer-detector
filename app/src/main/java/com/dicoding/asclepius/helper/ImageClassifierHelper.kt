package com.dicoding.asclepius.helper

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException


class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {
            val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)
            val baseOptionsBuilder = BaseOptions.builder()
                .setNumThreads(4)
            optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IOException) {
            classifierListener?.onError("Failed to load TensorFlow Lite model.")
            Log.e(TAG, "Error loading TensorFlow Lite model: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val results = imageClassifier?.classify(tensorImage)
            classifierListener?.onResults(results, 0)
        } catch (e: IOException) {
            classifierListener?.onError("Error processing image: ${e.message}")
            Log.e(TAG, "Error processing image: ${e.message}")
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}


