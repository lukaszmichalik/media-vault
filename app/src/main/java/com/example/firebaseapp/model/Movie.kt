package com.example.firebaseapp.model

class Movie(val id: String, val name: String, val rating: Int){
    constructor():this("","",0){}
}