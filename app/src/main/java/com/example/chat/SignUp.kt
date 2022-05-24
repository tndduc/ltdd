package com.example.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.databinding.ActivityProfileBinding
import com.example.chat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SignUp : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var biding : ActivitySignUpBinding
    private lateinit var storage : FirebaseStorage
    private lateinit var selectionImg : Uri
    var name =  ""
    var email = ""
    var password =""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        biding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(biding.root)
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        biding.btnSignUp.setOnClickListener{
            name = biding.edtName.text.toString()
            email = biding.edtEmail.text.toString()
            password = biding.edtPassword.text.toString()
            val reference  = storage.reference.child("Profile").child(Date().time.toString())
            reference.putFile(selectionImg).addOnCompleteListener{
                if(it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener{ task ->
                        signUp(task.toString())

                    }
                }
            }

        }
        biding.imgAvatar.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
    }

    private fun signUp(imgUrl :String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!,imgUrl)
                    val uid = FirebaseAuth.getInstance().currentUser!!
                        .uid
                    val intent = Intent(this@SignUp,ScanCode::class.java)
                    intent.putExtra("name",name)
                    intent.putExtra("uid",uid)
                    finish()
                    startActivity(intent)

                } else {

                    Toast.makeText(this@SignUp,name + email +password,Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String,uid: String,imgUrl:String){
        val codePartner = ""
        mDbRef= FirebaseDatabase.getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid,codePartner,imgUrl))

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