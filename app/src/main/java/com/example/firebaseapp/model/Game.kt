package com.example.firebaseapp.model

class Game (val id: String, val title: String, val publisher: String, val year: String, val played: Boolean,val rating: Int ){
    constructor():this("","","","",false,0){}
}