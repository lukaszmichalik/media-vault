package com.example.firebaseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.util.Log
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

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewUpdate = view.findViewById<TextView>(R.id.textViewUpdate)
        val deleteButton=view.findViewById<ImageView>(R.id.deletebutton)

        val movie = movieList[position]
        textViewName.text =movie.name

        textViewUpdate.setOnClickListener {
            showUpdateDialog(movie)
        }

        deleteButton.setOnClickListener {
            Log.d("Tagg","clicked")
            deleteMovie(movie)
        }


        return view
    }

    private fun deleteMovie(movie: Movie) {

        val name:String=movie.name
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are You Sure to Delete: $name ?")
            .setPositiveButton("Yes") { dialog, which ->
                val dbMovie = FirebaseDatabase.getInstance().getReference("movies").child(movie.id)
                dbMovie.removeValue()
                    Toast.makeText(context,"Product $name Deleted", Toast.LENGTH_SHORT).show()
                }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    private fun showUpdateDialog(movie: Movie) {

        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update Movie")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_movie, null)

        val editText= view.findViewById<TextInputEditText>(R.id.editTextUpdate)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val textInputLayout = view.findViewById(R.id.text_input_layout) as TextInputLayout

        editText.setText(movie.name)
        ratingBar.rating = movie.rating.toFloat()

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
                val name = editText.text.toString().trim()

                if(name.isEmpty()){
                    textInputLayout.error="Please enter name"
                    editText.requestFocus()
                }else {
                    val movie2 = Movie(movie.id, name, ratingBar.rating.toInt())

                    dbMovie.child(movie.id).setValue(movie2)

                    Toast.makeText(mCtx, "Movie updated", Toast.LENGTH_LONG).show()
                    Log.d("tag3: ", "dziala")

                    dialog.dismiss()

                }

            }
    }
}
