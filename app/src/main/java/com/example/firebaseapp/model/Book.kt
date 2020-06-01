package com.example.firebaseapp.model

class Book (val id: String, val title: String, val author: String, val year: String, val read: Boolean,val rating: Int ){
    constructor():this("","","","",false,0){}
}