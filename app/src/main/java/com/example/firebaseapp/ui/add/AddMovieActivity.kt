package com.example.firebaseapp.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


class AddMovieActivity : AppCompatActivity() {
    lateinit var  editTextTitle: TextInputEditText
    lateinit var  editTextDirector: EditText
    lateinit var  textViewYear: TextView
    lateinit var  watchedSwitch : Switch
    lateinit var  ratingBar: RatingBar
    lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        editTextTitle= findViewById(R.id.editTextTitleAdd)
        editTextDirector=findViewById(R.id.editTextDirectorAdd)
        textViewYear= findViewById(R.id.textViewYearAdd)
        watchedSwitch=findViewById(R.id.watchedSwitchAdd)
        ratingBar= findViewById(R.id.ratingBarAdd)
        buttonAdd= findViewById(R.id.addButton)

        val watchedSwitchState: Boolean = watchedSwitch.isChecked


        textViewYear.text = SimpleDateFormat("yyyy").format(System.currentTimeMillis())

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textViewYear.text = sdf.format(cal.time)

        }

        textViewYear.setOnClickListener {
            DatePickerDialog(this@AddMovieActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }


        buttonAdd.setOnClickListener {
            saveMovie()
        }

    }

    private fun saveMovie() {
        val title = editTextTitle.text.toString().trim()
        val textInputLayoutAddTitle = findViewById<TextInputLayout>(R.id.text_input_layout_add_title)
        val director = editTextDirector.text.toString().trim()
        val textInputLayoutAddDirector = findViewById<TextInputLayout>(R.id.text_input_layout_add_director)
        val watchedSwitchState: Boolean = watchedSwitch.isChecked

        if(title.isEmpty() || director.isEmpty()){
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
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("movies")
            val movieId = ref.push().key
            val movie = movieId?.let {
                Movie(
                    movieId,
                    title,
                    director,
                    textViewYear.text.toString(),
                    watchedSwitchState,
                    ratingBar.rating.toInt()
                )
            }

            if (movieId != null) {
                ref.child(movieId).setValue(movie).addOnCompleteListener {
                    //Toast.makeText(this, "movie saved successfully", Toast.LENGTH_LONG).show()
                }
            }
            finish()
        }
    }
    
}
