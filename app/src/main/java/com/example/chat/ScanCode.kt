package com.example.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ScanCode : AppCompatActivity() {
    private lateinit var editCode: EditText
    private lateinit var btnConnect: Button
    private lateinit var edtShowCode: TextView
    private lateinit var btnGetCode: Button
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code)
        supportActionBar?.hide()
        val name = intent.getStringExtra("name")
        val userUid = intent.getStringExtra("uid")
        editCode = findViewById(R.id.code_partner)
        btnConnect = findViewById(R.id.btnCode)
        edtShowCode = findViewById(R.id.userCode)
        btnGetCode = findViewById(R.id.btnGetCode)
        btnGetCode.setOnClickListener {
            edtShowCode.setText(userUid)
        }
        edtShowCode.setOnClickListener{
            val textToCopy = edtShowCode.text
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Love code copied to clipboard" , Toast.LENGTH_LONG).show()
        }
        btnConnect.setOnClickListener{
            mDbRef = FirebaseDatabase.
            getInstance("https://chatapplication-c1290-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("user")
            if (userUid != null) {
                val codepartner = editCode.text.toString()
                mDbRef.child(userUid).child("codePartner").setValue(codepartner)
                mDbRef.child(codepartner).child("codePartner").setValue(userUid)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var intent = Intent(this@ScanCode,MainActivity::class.java)
                            intent.putExtra("name",name)
                            intent.putExtra("uid",userUid)
                            intent.putExtra("partnerId",codepartner)
                            finish()
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,"Code does not exist",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}