package com.example.firebaseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie

class MovieAdapter(val mCtx:Context,val layoutResId:Int,val movieList:List<Movie>)
    : ArrayAdapter<Movie>(mCtx,layoutResId,movieList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewName = view.findViewById<TextView>(R.id.textViewName)

        val movie = movieList[position]

        textViewName.text =movie.name
        return view
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
