package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(imageUri)
        val categoryString = intent.getStringExtra(CATEGORY)
        val scoreString = intent.getStringExtra(SCORE)
        val imageUri = Uri.parse(imageUriString)
        val formattedScore = scoreString?.let {
            val value = it.toDouble()
            String.format("%.2f", value).replaceFirst("0.", "")
        } ?: "0"
        val formattedText = "$categoryString $formattedScore% "

        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = formattedText

    }

    companion object {
        const val CATEGORY = "category"
        const val SCORE = "score"
        const val imageUri = "image_uri"
    }
}