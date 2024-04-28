package com.example.homeassignment2.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.homeassignment2.R

// Secondary activity for viewing more info on a word.
class WordViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Additional internal function for convenient string processing.
        fun String.putIn(start: String = "", end: String = ""): String = start + this + end

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_view)

        // Add action for the return button
        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        // All the next lines of the method are for filling in the contents.
        val wordNameView = findViewById<TextView>(R.id.wordNameView)
        val partOfSpeechView = findViewById<TextView>(R.id.partOfSpeechView)
        val transcriptionView = findViewById<TextView>(R.id.transcriptionView)
        val definitionView = findViewById<TextView>(R.id.definitionView)
        val translationsView = findViewById<TextView>(R.id.translationsView)
        val examplesView = findViewById<TextView>(R.id.examplesView)
        val originView = findViewById<TextView>(R.id.originView)

        val wordName = intent.getStringExtra("word") ?: ""
        val partOfSpeech = intent.getStringExtra("part_of_speech")
        val transcription = intent.getStringExtra("transcription")
        val definition = intent.getStringExtra("definition")
        val translations = intent.getStringExtra("translations")
        val examples = intent.getStringExtra("examples")
        val origin = intent.getStringExtra("origin")

        wordNameView.text = wordName.toString()
        addContent(partOfSpeechView, partOfSpeech)
        addContent(transcriptionView, transcription?.putIn("/", "/"))
        addContent(definitionView, definition?.putIn(start = "DEFINITION:\n"))
        addContent(translationsView, translations?.putIn(start = "Translations: "))
        addContent(examplesView, examples?.putIn(start = "Examples:\n\t"))
        addContent(originView, origin?.putIn(start = "Origin: "))
    }

    // Adds the given string to the view if the string is present, and makes the view invisible otherwise.
    private fun addContent(view: TextView, content: String?) {
        if (content != null) {
            view.text = content
        } else {
            view.isVisible = false
        }
    }
}