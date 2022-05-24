package com.example.chat

class Note {
    var note: String?=null
    var uid: String?= null
    var date: String?=null
    constructor(){

    }
    constructor(note: String?,uid : String?,date:String?){
        this.note=note
        this.uid=uid
        this.date=date
    }
}