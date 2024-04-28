package com.example.homeassignment2.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.homeassignment2.dictionary.Dictionary
import com.example.homeassignment2.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Activity for adding a word to the list.
class WordAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_add)

        val nameView = findViewById<EditText>(R.id.nameEditText)
        val partOfSpeechSpinner = findViewById<Spinner>(R.id.partOfSpeechSpinner)
        val transcriptionView = findViewById<EditText>(R.id.transcriptionEditText)
        val definitionView = findViewById<EditText>(R.id.definitionEditText)
        val translationsView = findViewById<EditText>(R.id.translationsEditText)
        val examplesView = findViewById<EditText>(R.id.examplesEditText)
        val originView = findViewById<EditText>(R.id.originEditText)

        val findButton = findViewById<Button>(R.id.findButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        // Action upon clicking find button.
        // It will load the first found entrance of the word in the online dictionary.
        findButton.setOnClickListener {
            val partOfSpeechValues = partOfSpeechSpinner.context.resources.getStringArray(R.array.parts_of_speech)
            val job = GlobalScope.launch {
                val word = nameView.editableText.toString()
                runOnUiThread { ProgressDialog.create(it.context) }

                try {
                    val data = Dictionary.getWordData(word)

                    runOnUiThread { ProgressDialog.close() }
                    if (data == null) {
                        runOnUiThread { // Showing alert on failure
                            AlertDialog.Builder(it.context)
                                .setTitle("Error")
                                .setMessage("Failed to get information about word $word")
                                .setNeutralButton("Ok") { i: DialogInterface, _: Int -> i.dismiss() }
                                .create()
                                .show()
                        }
                    } else if (data.isNotEmpty()) {
                        runOnUiThread {
                            val data = data[0] // Shadowing here, it is intentional
                            val partOfSpeechInt = partOfSpeechValues.indexOf(data.partOfSpeech)
                            if (partOfSpeechInt != -1)
                                partOfSpeechSpinner.setSelection(partOfSpeechInt)
                            transcriptionView.setText(data.transcription ?: "")
                            definitionView.setText(data.definition)
                            translationsView.setText(data.translations)
                            examplesView.setText(data.examples)
                            originView.setText(data.origin ?: "")
                        }
                    }
                } catch (e: Exception) {
                    Log.d("ERROR", e.toString())
                }
            }
        }

        // Action upon clicking cancelation button.
        cancelButton.setOnClickListener {
            assureUserDecision(it, "Cancel", "Are you sure you want to cancel the addition? All the typed data would be lost.") {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        // Action upon clicking confirmation button - returning the results.
        confirmButton.setOnClickListener {
            // TODO: check that all the necessary fields were filled in
            assureUserDecision(it, "Add word", "Do wou really want to add the word?") {
                val dataIntent = Intent()

                dataIntent.putExtra("word", nameView.editableText.toString())
                dataIntent.putExtra("part_of_speech", partOfSpeechSpinner.selectedItem.toString())
                dataIntent.putExtra("transcription", transcriptionView.editableText.toString())
                dataIntent.putExtra("definition", definitionView.editableText.toString())
                dataIntent.putExtra("translations", translationsView.editableText.toString())
                dataIntent.putExtra("examples", examplesView.editableText.toString())
                dataIntent.putExtra("origin", originView.editableText.toString())

                setResult(RESULT_OK, dataIntent)
                finish()
            }
        }
    }

    // Auxiliary method that asks if the user is sure about their decision.
    private fun assureUserDecision(view: View, title: String, message: String, action: () -> Unit) {
        AlertDialog.Builder(view.context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _: DialogInterface, _: Int -> action() }
            .setNegativeButton("No") { _: DialogInterface, _: Int -> }
            .create()
            .show()
    }
}