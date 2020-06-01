package com.example.firebaseapp.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Switch
import android.widget.Toast
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class AddBookActivity : AppCompatActivity() {

    lateinit var  editTextTitle: TextInputEditText
    lateinit var  editTextAuthor: TextInputEditText
    lateinit var  editTextYear: TextInputEditText
    lateinit var  readSwitch : Switch
    lateinit var  ratingBar: RatingBar
    lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        editTextTitle= findViewById(R.id.editTextTitleAdd)
        editTextAuthor=findViewById(R.id.editTextAuthorAdd)
        editTextYear= findViewById(R.id.editTextYearAdd)
        readSwitch=findViewById(R.id.readSwitchAdd)
        ratingBar= findViewById(R.id.ratingBarAdd)
        buttonAdd= findViewById(R.id.addButton)

        ratingBar.isEnabled = false
        readSwitch.setOnClickListener {
            ratingBar.isEnabled = readSwitch.isChecked
            if(!readSwitch.isChecked){
                ratingBar.rating = 0f
            }
        }

        buttonAdd.setOnClickListener {
            saveBook()
        }

    }

    private fun saveBook() {
        val title = editTextTitle.text.toString().trim()
        val textInputLayoutAddTitle =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_title)

        val author = editTextAuthor.text.toString().trim()
        val textInputLayoutAddAuthor =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_author)

        val year = editTextYear.text.toString().trim()
        val textInputLayoutAddYear = findViewById<TextInputLayout>(R.id.text_input_layout_add_year)

        val readSwitchState: Boolean = readSwitch.isChecked

        if(title.isEmpty() || author.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1500) {
            if(title.isEmpty()){
                textInputLayoutAddTitle.error = "Please enter title"
            }else{
                textInputLayoutAddTitle.error = null
            }
            if(author.isEmpty()){
                textInputLayoutAddAuthor.error = "Please enter author"
            }else{
                textInputLayoutAddAuthor.error = null
            }
            if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1500){
                textInputLayoutAddYear.error = "Wrong Year"
            }else{
                textInputLayoutAddYear.error = null
            }
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("books")
            val bookId = ref.push().key
            val book = bookId?.let {
                Book(
                    bookId,
                    title,
                    author,
                    year,
                    readSwitchState,
                    ratingBar.rating.toInt()
                )
            }

            if (bookId != null) {
                ref.child(bookId).setValue(book).addOnCompleteListener {
                    Toast.makeText(this, "book saved successfully", Toast.LENGTH_LONG).show()
                }
            }
            finish()
        }
    }
}
