package com.example.firebaseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Game
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class GameAdapter(val mCtx: Context, val layoutResId:Int, val gameList:List<Game>)
    : ArrayAdapter<Game>(mCtx,layoutResId,gameList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitleRow = view.findViewById<TextView>(R.id.textViewTitle_row)
        val textViewPublisherRow = view.findViewById<TextView>(R.id.textViewPublisher_row)
        val textViewYearRow = view.findViewById<TextView>(R.id.textViewYear_row)
        val ratingBarRow = view.findViewById<RatingBar>(R.id.ratinBar_row)
        val playedImageButtonRow = view.findViewById<ImageButton>(R.id.playedImageButton_row)
        val textViewUpdateRow = view.findViewById<TextView>(R.id.textViewUpdate_row)
        val deleteButtonRow=view.findViewById<ImageButton>(R.id.deleteButton_row)

        val game = gameList[position]
        textViewTitleRow.text =game.title
        textViewPublisherRow.text=game.publisher
        textViewYearRow.text=game.year
        ratingBarRow.rating=game.rating.toFloat()

        if(!game.played){
            playedImageButtonRow.setImageResource(R.drawable.baseline_visibility_off_black_24dp)
            ratingBarRow.isVisible=false
        }

        textViewUpdateRow.setOnClickListener {
            showUpdateDialog(game)
        }

        deleteButtonRow.setOnClickListener {
            deleteGame(game)
        }

        return view
    }

    private fun deleteGame(game: Game) {
        val title:String=game.title
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are You Sure to Delete: $title ?")
            .setPositiveButton("Yes") { dialog, which ->
                val dbGame = FirebaseDatabase.getInstance().getReference("games").child(game.id)
                dbGame.removeValue()
                Toast.makeText(context,"$title Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    private fun showUpdateDialog(game: Game) {

        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update Game")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_game, null)

        val editTextTitleUpdate= view.findViewById<TextInputEditText>(R.id.editTextTitleUpdate)
        val textInputLayoutTitleUpdate = view.findViewById(R.id.text_input_layout_update_title) as TextInputLayout

        val editTextPublisherUpdate= view.findViewById<TextInputEditText>(R.id.editTextPublisherUpdate)
        val textInputLayoutPublisherUpdate = view.findViewById(R.id.text_input_layout_update_publisher) as TextInputLayout
        val editTextYearUpdate = view.findViewById<TextInputEditText>(R.id.editTextUpdateYear)
        val textInputLayoutYearUpdate = view.findViewById(R.id.text_input_layout_update_year) as TextInputLayout

        val playedSwitch = view.findViewById<Switch>(R.id.playedSwitchUpdate)

        val ratingBarUpdate = view.findViewById<RatingBar>(R.id.ratingBarUpdate)

        editTextTitleUpdate.setText(game.title)
        editTextPublisherUpdate.setText(game.publisher)
        editTextYearUpdate.setText(game.year)
        playedSwitch.isChecked = game.played
        ratingBarUpdate.rating = game.rating.toFloat()

        if(!playedSwitch.isChecked){
            ratingBarUpdate.rating = 0f
            ratingBarUpdate.isEnabled = false
        }

        playedSwitch.setOnClickListener {
            ratingBarUpdate.isEnabled = playedSwitch.isChecked
            if(!playedSwitch.isChecked){
                ratingBarUpdate.rating = 0f
            }
        }

        builder.setView(view)

        builder.setPositiveButton("Update") { p0, p1 ->
        }

        builder.setNegativeButton("No") { p0, p1 ->
        }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {

                val dbGame = FirebaseDatabase.getInstance().getReference("games")
                val title = editTextTitleUpdate.text.toString().trim()
                val publisher = editTextPublisherUpdate.text.toString().trim()
                val year = editTextYearUpdate.text.toString().trim()

                if(title.isEmpty() || publisher.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1958) {
                    if(title.isEmpty()){
                        textInputLayoutTitleUpdate.error = "Please enter title"
                    }else{
                        textInputLayoutTitleUpdate.error = null
                    }
                    if(publisher.isEmpty()){
                        textInputLayoutPublisherUpdate.error = "Please enter publisher"
                    }else{
                        textInputLayoutPublisherUpdate.error = null
                    }
                    if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1958){
                        textInputLayoutYearUpdate.error = "Wrong Year"
                    }else{
                        textInputLayoutYearUpdate.error = null
                    }
                } else {
                    val game2 = Game(game.id, title, publisher,year,playedSwitch.isChecked,ratingBarUpdate.rating.toInt())

                    dbGame.child(game.id).setValue(game2)

                    Toast.makeText(mCtx, "Game updated", Toast.LENGTH_LONG).show()

                    dialog.dismiss()
                }
            }
    }
}