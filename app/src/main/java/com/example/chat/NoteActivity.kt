package com.example.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class NoteActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var noteBox: EditText
    private lateinit var btnNote: Button
    private lateinit var mDbRef: DatabaseReference
private lateinit var store : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val uId = intent.getStringExtra("uid")

        mAuth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        mDbRef = FirebaseDatabase
            .getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference()
        noteBox = findViewById(R.id.edt_note)
        btnNote = findViewById(R.id.btnNote)
        fun getCurrentDate():String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }
        btnNote.setOnClickListener{
            var date = getCurrentDate()
            var note = noteBox.text.toString()
            val noteObject = Note(note,uId,date)

            val noteColection = store.collection("Notes")
            noteColection.document().set(noteObject)
        }

    }
}