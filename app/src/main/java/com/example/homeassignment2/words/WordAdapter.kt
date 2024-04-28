package com.example.homeassignment2.words

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homeassignment2.activities.WordViewActivity
import com.example.homeassignment2.R

// Class that manages the RecyclerView contents.
class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>(), View.OnClickListener {
    // An internal class for, as its name suggests, word view (a single item on a list) holding
    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val wordView = view.findViewById<TextView>(R.id.wordView)
        val partOfSpeechView = view.findViewById<TextView>(R.id.itemPartOfSpeechView)
        val translationView = view.findViewById<TextView>(R.id.translationView)
    }

    // Creating a list of displayable words.
    // TODO: create a separate data type for showing in a list, since some values would be shown in a separate view.
    private var data: MutableList<WordData> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Since it is a mock as of now, just load some words manually.
    init {
        // Let's add 2 sample words just to show that it all works
        data.add(
            WordData(
                word = "word",
                partOfSpeech = "noun",
                transcription = "wɜːd",
                audioExample = null,
                definition = "a group of letters or sounds that mean something, or a single letter or sound that means something",
                translations = "слово",
                examples = "'Hund' is the German word for 'dog'.\t\nHe has difficulty spelling long words.",
                origin = "From Middle English word, from Old English word, from Proto-West Germanic *word, from Proto-Germanic *wurdą, from Proto-Indo-European *wr̥dʰh₁om. Doublet of verb and verve; further related to vrata."
            )
        )
    }

    // Called upon ViewHolder creation, as its name suggests.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_item, parent, false)

        view.setOnClickListener(this)
        return WordViewHolder(view)
    }

    // Just a getter for the number of items currently stored in the class.
    override fun getItemCount(): Int = data.size

    // Called upon binding a ViewHolder to a specific position.
    // Fills the holder with data.
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = data[position]

        holder.itemView.tag = word
        holder.wordView.text = word.word
        holder.partOfSpeechView.text = word.partOfSpeech.uppercase()
        holder.translationView.text = word.translations

        holder.itemView.setOnClickListener(this)
    }

    // Called when a word is clicked by the user.
    override fun onClick(v: View?) {
        if (v != null && v.tag is WordData) {
            val word = v.tag as WordData
            val intent = Intent(v.context, WordViewActivity::class.java)

            intent.putExtra("word", word.word)
            intent.putExtra("part_of_speech", word.partOfSpeech)
            intent.putExtra("transcription", word.transcription)
            intent.putExtra("definition", word.definition)
            intent.putExtra("translations", word.translations)
            intent.putExtra("examples", word.examples)
            intent.putExtra("origin", word.origin)

            v.context.startActivity(intent)
        }
    }

    // Adds a new word to the list.
    fun addWord(word: WordData) {
        data.add(word)
        notifyItemInserted(data.size - 1)
    }
}