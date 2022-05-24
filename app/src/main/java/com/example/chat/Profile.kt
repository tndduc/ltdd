package com.example.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.databinding.ActivityProfileBinding
import com.example.chat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*


class Profile : AppCompatActivity() {
    private lateinit var biding : ActivityProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var ref: DatabaseReference
    private lateinit var storage : FirebaseStorage
    private lateinit var selectionImg : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val myName = intent.getStringExtra("name")
        var pic = ""
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()
        biding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(biding.root)
        var uid=""
        var name =""
        var email =""
        mDbRef = FirebaseDatabase.
        getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user")

        val user = FirebaseAuth.getInstance().currentUser

        var ref = FirebaseDatabase.
        getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference()
        ref.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser?.uid == uid){
                        pic = currentUser?.photoUrl.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        biding.logout.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(this@Profile,Login::class.java)
            finish()
            startActivity(intent)
        }
        biding.imgAvatar.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
        user?.let {
            // Name, email address, and profile photo Url\
            email = user.email.toString()
            uid = user.uid
            name = user.displayName.toString()
            biding.edtEmail.setText(email)
            biding.edtName.setText(myName)

            Toast.makeText(this, pic, Toast.LENGTH_SHORT).show()
        }
        biding.btnUpdate.setOnClickListener{
            val nameEdit = biding.edtName.text.toString()
            val emailEdit = biding.edtEmail.text.toString()
            val reference  = storage.reference.child("Profile").child(Date().time.toString())
            reference.putFile(selectionImg).addOnCompleteListener{
                if(it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener{ task ->
                        mDbRef.child(uid).child("name").setValue(nameEdit)
                        mDbRef.child(uid).child("email").setValue(emailEdit)
                        mDbRef.child(uid).child("photoUrl").setValue(task.toString())
                    }
                }
            }
            mDbRef.child(uid).child("name").setValue(nameEdit)
            mDbRef.child(uid).child("email").setValue(emailEdit)
            Toast.makeText(this, "Done ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Profile,MainActivity::class.java)
            finish()
            this.startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            if(data.data!=null){
                selectionImg = data.data!!
                biding.imgAvatar.setImageURI(selectionImg)
            }
        }
    }



}