package com.example.firebaseapp.model

class Movie(val id: String, val title: String, val director: String, val year: String, val watched: Boolean,val rating: Int ){
    constructor():this("","","","",false,0){}
}