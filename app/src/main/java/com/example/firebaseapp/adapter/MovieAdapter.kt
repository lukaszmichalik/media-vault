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
        /*val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Delete Movie")
        val dbMovie = FirebaseDatabase.getInstance().getReference("movies")*/

        val name:String=movie.name
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are You Sure to Delete: $name ?")
            .setPositiveButton("Yes") { dialog, which ->
                val dbMovie = FirebaseDatabase.getInstance().getReference("movies").child(movie.id)
                dbMovie.removeValue()
                    Toast.makeText(context,"Product $movie.name Deleted", Toast.LENGTH_SHORT).show()
                }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    private fun showUpdateDialog(movie: Movie) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Movie")
        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_movie, null)

        //val editText = view.findViewById<>(R.id.editTextUpdate)
        val editText= view.findViewById<TextInputEditText>(R.id.editTextUpdate)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val textInputLayout = view.findViewById(R.id.text_input_layout) as TextInputLayout

        /*editText.setText(movie.name)*/
        editText.setText(movie.name)
        /*val inputText = editText.editText?.text.toString()*/
        ratingBar.rating = movie.rating.toFloat()

        builder.setView(view)

        builder.setPositiveButton("Update") { p0, p1 ->
            val dbMovie = FirebaseDatabase.getInstance().getReference("movies")
            /*val name = editText.text.toString().trim()*/
            val name = editText.text.toString().trim()

            if(name.isEmpty()){
                textInputLayout.error="Please enter name"
                editText.requestFocus()
            }
            val movie2 = Movie(movie.id, name, ratingBar.rating.toInt())

            dbMovie.child(movie.id).setValue(movie2)

            Toast.makeText(mCtx,"Movie updated", Toast.LENGTH_LONG).show()
        }

        builder.setNegativeButton("No") { p0, p1 ->
        }
         val dialog = builder.create()
         dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                val dbMovie = FirebaseDatabase.getInstance().getReference("movies")
                /*val name = editText.text.toString().trim()*/
                val name = editText.text.toString().trim()

                if(name.isEmpty()){
                    textInputLayout.error="Please enter name"
                    editText.requestFocus()
                }else {
                    val movie2 = Movie(movie.id, name, ratingBar.rating.toInt())

                    dbMovie.child(movie.id).setValue(movie2)

                    Toast.makeText(mCtx, "Movie updated", Toast.LENGTH_LONG).show()
                    Log.d("tag3: ", "dziala")
                    val wantToCloseDialog = false
                    //Do stuff, possibly set wantToCloseDialog to true then...

                    // showToast

                    dialog.dismiss()
                    // other stuff to do
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
    }
}



/*class MovieAdapter (val mCtx:Context,val layoutResId:Int,val movieList:MutableList<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.movies, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var movie=movieList[position]
        holder.movieName.setText(holder.adapterPosition)
        holder.movieRatingBar.rating= movie.rating.toFloat()
    }*/

  /*  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewName = view.findViewById<TextView>(R.id.textView)

        val movie = movieList[position]

        textViewName.text = movie.name
        return view
    }*/

    /*class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val movieView=view
        var movieName=view.findViewById<EditText>(R.id.textViewName)
        var movieRatingBar=view.findViewById<RatingBar>(R.id.ratingBar)
        //var deleteButton=view.findViewById<Button>(R.id.deleteButton)
    }}
*/
