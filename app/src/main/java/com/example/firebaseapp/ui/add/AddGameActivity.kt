package com.example.firebaseapp.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Switch
import android.widget.Toast
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Game
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class AddGameActivity : AppCompatActivity() {
    lateinit var  editTextTitle: TextInputEditText
    lateinit var  editTextPublisher: TextInputEditText
    lateinit var  editTextYear: TextInputEditText
    lateinit var  playedSwitch : Switch
    lateinit var  ratingBar: RatingBar
    lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        editTextTitle= findViewById(R.id.editTextTitleAdd)
        editTextPublisher=findViewById(R.id.editTextPublisherAdd)
        editTextYear= findViewById(R.id.editTextYearAdd)
        playedSwitch=findViewById(R.id.playedSwitchAdd)
        ratingBar= findViewById(R.id.ratingBarAdd)
        buttonAdd= findViewById(R.id.addButton)

        ratingBar.isEnabled = false
        playedSwitch.setOnClickListener {
            ratingBar.isEnabled = playedSwitch.isChecked
            if(!playedSwitch.isChecked){
                ratingBar.rating = 0f
            }
        }

        buttonAdd.setOnClickListener {
            saveGame()
        }

    }

    private fun saveGame() {
        val title = editTextTitle.text.toString().trim()
        val textInputLayoutAddTitle =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_title)
        val publisher = editTextPublisher.text.toString().trim()
        val textInputLayoutAddPublisher =
            findViewById<TextInputLayout>(R.id.text_input_layout_add_publisher)
        val year = editTextYear.text.toString().trim()
        val textInputLayoutAddYear = findViewById<TextInputLayout>(R.id.text_input_layout_add_year)
        val playedSwitchState: Boolean = playedSwitch.isChecked

        if(title.isEmpty() || publisher.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1958) {
            if(title.isEmpty()){
                textInputLayoutAddTitle.error = "Please enter title"
            }else{
                textInputLayoutAddTitle.error = null
            }
            if(publisher.isEmpty()){
                textInputLayoutAddPublisher.error = "Please enter publisher"
            }else{
                textInputLayoutAddPublisher.error = null
            }
            if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1958){
                textInputLayoutAddYear.error = "Wrong Year"
            }else{
                textInputLayoutAddYear.error = null
            }
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("games")
            val gameId = ref.push().key
            val game = gameId?.let {
                Game(
                    gameId,
                    title,
                    publisher,
                    year,
                    playedSwitchState,
                    ratingBar.rating.toInt()
                )
            }

            if (gameId != null) {
                ref.child(gameId).setValue(game).addOnCompleteListener {
                    Toast.makeText(this, "game saved successfully", Toast.LENGTH_LONG).show()
                }
            }
            finish()
        }
    }
}