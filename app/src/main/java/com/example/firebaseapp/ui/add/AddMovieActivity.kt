package com.example.firebaseapp.ui.add

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class AddMovieActivity : AppCompatActivity() {
    lateinit var  editTextTitle: TextInputEditText
    lateinit var  editTextDirector: TextInputEditText
    lateinit var  editTextYear: TextInputEditText
    lateinit var  watchedSwitch : Switch
    lateinit var  ratingBar: RatingBar
    lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        editTextTitle= findViewById(R.id.editTextTitleAdd)
        editTextDirector=findViewById(R.id.editTextDirectorAdd)
        editTextYear= findViewById(R.id.editTextYearAdd)
        watchedSwitch=findViewById(R.id.watchedSwitchAdd)
        ratingBar= findViewById(R.id.ratingBarAdd)
        buttonAdd= findViewById(R.id.addButton)

        val watchedSwitchState: Boolean = watchedSwitch.isChecked

        buttonAdd.setOnClickListener {
            saveMovie()
        }

    }

    private fun saveMovie() {
        val title = editTextTitle.text.toString().trim()
        val textInputLayoutAddTitle =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_title)
        val director = editTextDirector.text.toString().trim()
        val textInputLayoutAddDirector =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_director)
        val year = editTextYear.text.toString().trim()
        val textInputLayoutAddYear = findViewById<TextInputLayout>(R.id.text_input_layout_add_year)
        val watchedSwitchState: Boolean = watchedSwitch.isChecked

        var canFinish = true

        /*if(title.isEmpty() || director.isEmpty()){
            if (title.isEmpty()){
                textInputLayoutAddTitle.error = "Please enter title"
                if (director.isNotEmpty()){
                    textInputLayoutAddDirector.error = null
                }
            }
            if(director.isEmpty()){
                textInputLayoutAddDirector.error = "Please enter director"
                if (title.isNotEmpty()){
                    textInputLayoutAddTitle.error = null}
            }
        }*/
        /*if(title.isEmpty()){
            textInputLayoutAddTitle.error = "Please enter title"
        }else{
            textInputLayoutAddTitle.error = null
            canFinish = true
        }

        if(director.isEmpty()){
            textInputLayoutAddDirector.error = "Please enter director"
            canFinish = false
        }else{
            textInputLayoutAddDirector.error = null
            canFinish = true
        }

        if(year.isEmpty()){
            textInputLayoutAddYear.error = "Please enter year"
            canFinish = false
        }else{
            textInputLayoutAddYear.error = null
            canFinish = true
        }

        if (year.toInt() > 2020 || year.toInt() < 1888) {
            textInputLayoutAddYear.error = "Wrong year"
            canFinish = false
        } else {
            textInputLayoutAddYear.error = null
            canFinish = true
        }*/

        if(title.isEmpty() || director.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888) {
            if(title.isEmpty()){
                textInputLayoutAddTitle.error = "Please enter title"
            }else{
                textInputLayoutAddTitle.error = null
            }
            if(director.isEmpty()){
                textInputLayoutAddDirector.error = "Please enter director"
            }else{
                textInputLayoutAddDirector.error = null
            }
            if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888){
                textInputLayoutAddYear.error = "Wrong Year"
            }else{
                textInputLayoutAddYear.error = null
            }
        } else {
                val ref = FirebaseDatabase.getInstance().getReference("movies")
                val movieId = ref.push().key
                val movie = movieId?.let {
                    Movie(
                        movieId,
                        title,
                        director,
                        year,
                        watchedSwitchState,
                        ratingBar.rating.toInt()
                    )
                }

                if (movieId != null) {
                    ref.child(movieId).setValue(movie).addOnCompleteListener {
                        Toast.makeText(this, "movie saved successfully", Toast.LENGTH_LONG).show()
                    }
                }
                finish()
        }
    }
}
