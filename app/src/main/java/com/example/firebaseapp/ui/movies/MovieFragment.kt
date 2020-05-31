package com.example.firebaseapp.ui.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.firebaseapp.R
import com.example.firebaseapp.adapter.MovieAdapter
import com.example.firebaseapp.model.Movie
import com.example.firebaseapp.ui.AddActivity
import com.google.firebase.database.*


class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    lateinit var ref: DatabaseReference
    lateinit var movieList: MutableList<Movie>
    lateinit var listView: ListView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        setHasOptionsMenu(true)
        movieList = mutableListOf()
        listView= root.findViewById(R.id.listView)
        ref = FirebaseDatabase.getInstance().getReference("movies")
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

                    val context = context
                    if (context !=null){
                        val adapter = MovieAdapter(context, R.layout.movies, movieList)
                        listView.adapter = adapter
                    }
                }else
                {
                    listView.adapter=null
                }
            }
        })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent= Intent(context, AddActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

}