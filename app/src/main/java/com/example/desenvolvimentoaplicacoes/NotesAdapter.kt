package com.example.desenvolvimentoaplicacoes

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desenvolvimentoaplicacoes.database.NotesDataBaseHelper.Companion.DESCRIPTION_NOTE
import com.example.desenvolvimentoaplicacoes.database.NotesDataBaseHelper.Companion.TITLE_NOTES

class NotesAdapter (private val listener: NoteClickListener): RecyclerView.Adapter<NotesViewHolder>(){
    private var nCursor: Cursor? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent,false))



    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        nCursor?.moveToPosition(position)

        holder.noteTitle.text = nCursor?.getString(nCursor?.getColumnIndex(TITLE_NOTES)as Int)
        holder.noteDescription.text = nCursor?.getString(nCursor?.getColumnIndex(DESCRIPTION_NOTE)as Int)
        holder.noteButtonRemove.setOnClickListener {
            nCursor?.moveToPosition(position)
            listener.noteRemoveItem(nCursor as Cursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener { listener.noteClickItem(nCursor as Cursor) }
    }

    override fun getItemCount(): Int = if(nCursor != null) nCursor?.count as Int else 0

    fun setCursor(newCursor: Cursor?){
        nCursor = newCursor
        notifyDataSetChanged()
    }
}

class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val noteTitle = itemView.findViewById(R.id.note_title) as TextView
    val noteDescription = itemView.findViewById(R.id.note_description) as TextView
    val noteButtonRemove = itemView.findViewById(R.id.note_button_remove) as Button
}