package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.fragments.HomeFragment
import com.example.chat.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter

    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var addNote: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase
            .getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference()
        userList = ArrayList()
        adapter = UserAdapter(this,userList)
        userRecyclerView = findViewById(R.id.user_RecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter
        //
        noteList = ArrayList()
        noteAdapter = NoteAdapter(this,noteList)
        noteRecyclerView = findViewById(R.id.note_RecyclerView)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.adapter = adapter


        val name = intent.getStringExtra("name")
        val uId = intent.getStringExtra("uid")
        var myName = ""
        var partnerId = ""
        var partnerName = ""
        var pic = ""
        var log =""
        addNote =  findViewById(R.id.floatingActionButton)
        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid == currentUser?.codePartner){
                        userList.add(currentUser!!)
                        partnerId = currentUser?.uid.toString()
                        partnerName = currentUser?.name.toString()
                    }
                    if (mAuth.currentUser?.uid == currentUser?.uid){
                        myName = currentUser?.name.toString()

                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


        ////////////////
//        mDbRef.child("notes")
//            .addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                noteList.clear()
//                for (postSnapshot in snapshot.children){
//                    val noteData = postSnapshot.getValue(Note::class.java)
//                        noteList.add(noteData!!)
//                        log=noteData.toString()
//
//                }
//                noteAdapter.notifyDataSetChanged()
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
        Toast.makeText(this, log, Toast.LENGTH_SHORT).show()
        ///////////////
        addNote.setOnClickListener{
            val intent = Intent(this,NoteActivity::class.java)
            intent.putExtra("name",partnerName)
            intent.putExtra("uid",partnerId)

            this.startActivity(intent)
        }
        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                }
                R.id.chatFragment -> {
                    val intent = Intent(this,ChatActivity::class.java)
                    intent.putExtra("name",partnerName)
                    intent.putExtra("uid",partnerId)
                    this.startActivity(intent)
                }
                R.id.profileFragment -> {
                    val intent = Intent(this,Profile::class.java)
                    intent.putExtra("name",myName)
                    intent.putExtra("pic",pic)

                    this.startActivity(intent)
                }
            }
            true
        }

    }

}


