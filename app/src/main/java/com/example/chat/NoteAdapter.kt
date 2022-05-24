package com.example.chat
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.log

class NoteAdapter(val context: Context, val notelist: ArrayList<Note>):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.note,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentUser = notelist[position]
        holder.textDate.text = currentUser.date
        holder.textNote.text = currentUser.note
        holder.itemView.setOnClickListener{
//            val intent = Intent(context,ChatActivity::class.java)
//            intent.putExtra("name",currentUser.note)
//            intent.putExtra("uid",currentUser.uid)
//            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return notelist.size
    }
    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textDate = itemView.findViewById<TextView>(R.id.txt_date)
        var textNote = itemView.findViewById<TextView>(R.id.txt_note)
    }
}
