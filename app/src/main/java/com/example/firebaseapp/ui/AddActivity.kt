package com.example.firebaseapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.firebaseapp.R
import com.example.firebaseapp.adapter.MovieAdapter
import com.example.firebaseapp.model.Movie
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*

class AddActivity : AppCompatActivity() {
    lateinit var  editText: TextInputEditText
    lateinit var  ratingBar: RatingBar
    lateinit var button: Button





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)



        editText= findViewById(R.id.editText)
        ratingBar= findViewById(R.id.ratingBar)
        button= findViewById(R.id.button)

        button.setOnClickListener {
            saveMovie()
        }

    }

    private fun saveMovie() {
        val name = editText.text.toString().trim()
        val textInputLayoutAdd = findViewById<TextInputLayout>(R.id.text_input_layout_add)

        if(name.isEmpty()){
            textInputLayoutAdd.error = "Please enter a name"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("movies")
        val movieId = ref.push().key
        val movie = movieId?.let { Movie(it,name, ratingBar.rating.toInt()) }

        if (movieId != null) {
            ref.child(movieId).setValue(movie).addOnCompleteListener {
                 //Toast.makeText(this, "movie saved successfully", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
