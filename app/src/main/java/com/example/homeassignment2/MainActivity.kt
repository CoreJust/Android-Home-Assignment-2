package com.example.homeassignment2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeassignment2.databinding.ActivityMainBinding

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

        val addWordButton = findViewById<ImageButton>(R.id.addWordButton)
        addWordButton.setOnClickListener {
            val intent = Intent(this, WordAddActivity::class.java)
            launcher.launch(intent)
        }
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
