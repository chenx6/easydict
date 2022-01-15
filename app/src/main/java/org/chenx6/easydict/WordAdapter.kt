package org.chenx6.easydict

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

import org.chenx6.easydict.entities.WordEntity;

class WordAdapter(private val context: Context, private var dataSet: Array<WordEntity>) :
    RecyclerView.Adapter<WordAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordView: TextView = view.findViewById(R.id.wordView)
        val translationView: TextView = view.findViewById(R.id.translationView)
        val wordItemView: MaterialCardView = view.findViewById(R.id.wordItemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.word_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = dataSet[position].word
        holder.wordView.text = word
        holder.translationView.text = dataSet[position].translation?.replace("\\n", "\n")
        holder.wordItemView.setOnClickListener {
            val intent = Intent(context, WordDetailActivity::class.java)
            intent.putExtra("word", word)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateDataset(newDataset: Array<WordEntity>) {
        dataSet = newDataset
        notifyDataSetChanged() // TODO Use more effect way
    }
}