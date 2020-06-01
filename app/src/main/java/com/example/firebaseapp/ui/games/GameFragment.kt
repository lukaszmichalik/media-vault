package com.example.firebaseapp.ui.games

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.firebaseapp.R
import com.example.firebaseapp.adapter.GameAdapter
import com.example.firebaseapp.model.Game
import com.example.firebaseapp.ui.add.AddGameActivity
import com.google.firebase.database.*

class GameFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var gameList: MutableList<Game>
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game, container, false)
        setHasOptionsMenu(true)

        gameList = mutableListOf()
        listView= root.findViewById(R.id.listView)
        ref = FirebaseDatabase.getInstance().getReference("games")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    gameList.clear()
                    for (h in p0.children){
                        val game = h.getValue(Game::class.java)
                        gameList.add(game!!)
                    }

                    val context = context
                    if (context !=null){
                        val adapter = GameAdapter(context, R.layout.games, gameList)
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
        val intent= Intent(context, AddGameActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}