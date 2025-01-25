package com.example.loginsignup

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivitySignUpBinding
import com.example.loginsignup.databinding.NoteItemBinding

class noteAdapter(private  val notes:List<NotesItem>, private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<noteAdapter.noteViewHolder>() {

    interface OnItemClickListener{
        fun onDeleteClick(noteId:String)
        fun onUpdateClick(noteId:String, title: String, des:String)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): noteViewHolder {
     val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return noteViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return  notes.size
    }

    override fun onBindViewHolder(holder: noteViewHolder, position: Int) {
    val note= notes[position]
        holder.bind(note)
        holder.binding.btnUpdate.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId, note.title, note.des)
        }
        holder.binding.btnDel.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }


    }

    class noteViewHolder(val binding: NoteItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NotesItem) {
            binding.textView4.text=note.title
            binding.textView5.text=note.des

        }

    }
}