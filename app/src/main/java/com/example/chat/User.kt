package com.example.chat

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class User(val name: String? = null, val email: String? = null,val uid: String? = null,val codePartner : String ?=null, val photoUrl:String?=null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}