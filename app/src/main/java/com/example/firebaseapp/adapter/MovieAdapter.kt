package com.example.firebaseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase


class MovieAdapter(val mCtx:Context,val layoutResId:Int,val movieList:List<Movie>)
    : ArrayAdapter<Movie>(mCtx,layoutResId,movieList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitleRow = view.findViewById<TextView>(R.id.textViewTitle_row)
        val textViewDirectorRow = view.findViewById<TextView>(R.id.textViewDirector_row)
        val textViewYearRow = view.findViewById<TextView>(R.id.textViewYear_row)
        val ratingBarRow = view.findViewById<RatingBar>(R.id.ratinBar_row)
        val watchedImageButtonRow = view.findViewById<ImageButton>(R.id.watchedImageButton_row)
        val textViewUpdateRow = view.findViewById<TextView>(R.id.textViewUpdate_row)
        val deleteButtonRow=view.findViewById<ImageButton>(R.id.deleteButton_row)


        val movie = movieList[position]
        textViewTitleRow.text =movie.title
        textViewDirectorRow.text=movie.director
        textViewYearRow.text=movie.year
        ratingBarRow.rating=movie.rating.toFloat()

        if(!movie.watched){
            watchedImageButtonRow.setImageResource(R.drawable.baseline_visibility_off_black_24dp)
        }


        textViewUpdateRow.setOnClickListener {
            showUpdateDialog(movie)
        }

        deleteButtonRow.setOnClickListener {
            deleteMovie(movie)
        }


        return view
    }

    private fun deleteMovie(movie: Movie) {
        val title:String=movie.title
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are You Sure to Delete: $title ?")
            .setPositiveButton("Yes") { dialog, which ->
                val dbMovie = FirebaseDatabase.getInstance().getReference("movies").child(movie.id)
                dbMovie.removeValue()
                    Toast.makeText(context,"$title Deleted", Toast.LENGTH_SHORT).show()
                }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    private fun showUpdateDialog(movie: Movie) {

        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update Movie")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_movie, null)

        val editTextTitleUpdate= view.findViewById<TextInputEditText>(R.id.editTextTitleUpdate)
        val textInputLayoutTitleUpdate = view.findViewById(R.id.text_input_layout_update_title) as TextInputLayout

        val editTextDirectorUpdate= view.findViewById<TextInputEditText>(R.id.editTextDirectorUpdate)
        val textInputLayoutDirectorUpdate = view.findViewById(R.id.text_input_layout_update_director) as TextInputLayout
        val editTextYearUpdate = view.findViewById<TextInputEditText>(R.id.editTextUpdateYear)
        val textInputLayoutYearUpdate = view.findViewById(R.id.text_input_layout_update_year) as TextInputLayout

        val watchedSwitch = view.findViewById<Switch>(R.id.watchedSwitchUpdate)


        val ratingBarUpdate = view.findViewById<RatingBar>(R.id.ratingBarUpdate)

        val watchedSwitchState: Boolean = watchedSwitch.isChecked

        editTextTitleUpdate.setText(movie.title)
        editTextDirectorUpdate.setText(movie.director)
        editTextYearUpdate.setText(movie.year)
        watchedSwitch.isChecked = movie.watched


        ratingBarUpdate.rating = movie.rating.toFloat()

        builder.setView(view)

        builder.setPositiveButton("Update") { p0, p1 ->
        }

        builder.setNegativeButton("No") { p0, p1 ->
        }

         val dialog = builder.create()
         dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {

                val dbMovie = FirebaseDatabase.getInstance().getReference("movies")
                val title = editTextTitleUpdate.text.toString().trim()
                val director = editTextDirectorUpdate.text.toString().trim()
                val year = editTextYearUpdate.text.toString().trim()

                if(title.isEmpty() || director.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888) {
                    if(title.isEmpty()){
                        textInputLayoutTitleUpdate.error = "Please enter title"
                    }else{
                        textInputLayoutTitleUpdate.error = null
                    }
                    if(director.isEmpty()){
                        textInputLayoutDirectorUpdate.error = "Please enter director"
                    }else{
                        textInputLayoutDirectorUpdate.error = null
                    }
                    if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888){
                        textInputLayoutYearUpdate.error = "Wrong Year"
                    }else{
                        textInputLayoutYearUpdate.error = null
                    }
                } else {
                        val movie2 = Movie(movie.id, title, director,year,watchedSwitch.isChecked,ratingBarUpdate.rating.toInt())

                        dbMovie.child(movie.id).setValue(movie2)

                        Toast.makeText(mCtx, "Movie updated", Toast.LENGTH_LONG).show()

                        dialog.dismiss()
                    }
                }
    }
}
