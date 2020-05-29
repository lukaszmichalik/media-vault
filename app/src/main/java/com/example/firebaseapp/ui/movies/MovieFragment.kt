package com.example.firebaseapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.firebaseapp.R
import com.example.firebaseapp.adapter.MovieAdapter
import com.example.firebaseapp.model.Movie
import com.google.firebase.database.*

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel

    lateinit var  editText: EditText
    lateinit var  ratingBar: RatingBar
    lateinit var button: Button

    lateinit var movieList: MutableList<Movie>
    lateinit var ref: DatabaseReference
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       /* homeViewModel =
            ViewModelProviders.of(this).get(MovieViewModel::class.java)*/
        val root = inflater.inflate(R.layout.fragment_movie, container, false)

        movieList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("movies")

        editText= root.findViewById(R.id.editText)
        ratingBar= root.findViewById(R.id.ratingBar)
        button= root.findViewById(R.id.button)
        listView= root.findViewById(R.id.listView)
        button.setOnClickListener {
            saveMovie()
        }
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    movieList.clear()
                    for (h in p0.children){
                        val movie = h.getValue(Movie::class.java)
                        movieList.add(movie!!)
                    }

                    /*root.list_of_products.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                    list_of_products.adapter=MovieAdapter(context!!, R.layout.movies, movieList)*/
                   /* val adapter = MovieAdapter(context!!,R.layout.movies, movieList)

                    listView.layoutManger = adapter*/
                    val context = context
                    if (context !=null){
                    val adapter = MovieAdapter(context, R.layout.movies, movieList)
                    listView.adapter = adapter}
                }
            }
        })
       /* homeViewModel.text.observe(this, Observer {

        })*/
        return root
    }

    private fun saveMovie() {
        val name = editText.text.toString().trim()

        if(name.isEmpty()){
            editText.error = "Please enter a name"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("movies")
        val movieId = ref.push().key
        val movie = movieId?.let { Movie(it,name, ratingBar.rating.toInt()) }

        if (movieId != null) {
            ref.child(movieId).setValue(movie).addOnCompleteListener {
               // Toast.makeText(context, "movie saved successfully", Toast.LENGTH_LONG).show()
            }
        }
    }
}