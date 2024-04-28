package com.example.homeassignment2.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeassignment2.dictionary.Dictionary
import com.example.homeassignment2.dictionary.makeWordDatas
import com.example.homeassignment2.R
import com.example.homeassignment2.words.WordAdapter
import com.example.homeassignment2.words.WordData
import com.example.homeassignment2.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = WordAdapter()
        binding.recyclerView.adapter = adapter

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    addWord(data)
                }
            }
        }

        val loadWordsButton = findViewById<ImageButton>(R.id.loadWordsButton)
        val addWordButton = findViewById<ImageButton>(R.id.addWordButton)

        loadWordsButton.setOnClickListener {
            onLoadWords()
        }

        addWordButton.setOnClickListener {
            val intent = Intent(this, WordAddActivity::class.java)
            launcher.launch(intent)
        }
    }

    // Called upon clicking on the button to add multiple words at once
    private fun onLoadWords() {
        val loadWordsDialog = AlertDialog.Builder(this)
            .setView(R.layout.load_words_dialog)
            .create()

        loadWordsDialog.show()

        val wordsEditText = loadWordsDialog.findViewById<EditText>(R.id.loadWordsEditText)
        val loadWordsButton = loadWordsDialog.findViewById<Button>(R.id.loadManyWordsButton)

        loadWordsButton?.setOnClickListener {
            val words = wordsEditText?.editableText.toString().split(";", ",", " ", ".", "\t", "\n").map { it.lowercase() }
            GlobalScope.launch {
                runOnUiThread { ProgressDialog.create(it.context) }

                try {
                    val failedWords = mutableListOf<String>()
                    val loadedWords = mutableListOf<String>()
                    val loadedWordDatas = mutableListOf<WordData>()
                    for (word in words) {
                        val wordDatas = loadWordDatasForWord(word)
                        if (wordDatas == null) {
                            failedWords.add(word)
                        } else {
                            loadedWords.add(word)
                            loadedWordDatas += wordDatas
                        }
                    }

                    runOnUiThread { ProgressDialog.close() }
                    if (failedWords.size > 0) {
                        runOnUiThread { // Showing alert on failures
                            AlertDialog.Builder(it.context)
                                .setTitle("Warning")
                                .setMessage("Failed to get information about words: " + failedWords.joinToString(", "))
                                .setNeutralButton("Ok") { i: DialogInterface, _: Int -> i.dismiss() }
                                .create()
                                .show()
                        }
                    }

                    if (loadedWordDatas.size > 0) {
                        runOnUiThread { // Showing alert on failures
                            AlertDialog.Builder(it.context)
                                .setTitle("Success")
                                .setMessage("Loaded following words: " + loadedWords.joinToString(", "))
                                .setNeutralButton("Ok") { i: DialogInterface, _: Int -> i.dismiss() }
                                .create()
                                .show()

                            for (wordData in loadedWordDatas) {
                                adapter.addWord(wordData)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("ERROR", e.toString())
                }
            }

            runOnUiThread {
                loadWordsDialog.dismiss()
            }
        }
    }

    private suspend fun loadWordDatasForWord(word: String): List<WordData>? {
        try {
            val data = Dictionary.getRawWordDatas(word)
            if (!data.isNullOrEmpty()) {
                val result = mutableListOf<WordData>()
                for (rawData in data) {
                    result += makeWordDatas(rawData) ?: listOf<WordData>()
                }

                return result
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }

        return null
    }

    private fun addWord(intent: Intent) {
        val word = WordData(
            word = intent.getStringExtra("word") ?: "",
            partOfSpeech = intent.getStringExtra("part_of_speech") ?: "",
            transcription = intent.getStringExtra("transcription") ?: "",
            audioExample = null,
            definition = intent.getStringExtra("definition") ?: "",
            translations = intent.getStringExtra("translations") ?: "",
            examples = intent.getStringExtra("examples") ?: "",
            origin = intent.getStringExtra("origin") ?: ""
        )

        adapter.addWord(word)
    }
}
