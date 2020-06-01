package com.example.firebaseapp.ui.books

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.firebaseapp.R
import com.example.firebaseapp.adapter.BookAdapter
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.ui.add.AddBookActivity
import com.google.firebase.database.*

class BookFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var bookList: MutableList<Book>
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_book, container, false)
        setHasOptionsMenu(true)
        bookList = mutableListOf()
        listView= root.findViewById(R.id.listView)
        ref = FirebaseDatabase.getInstance().getReference("books")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    bookList.clear()
                    for (h in p0.children){
                        val book = h.getValue(Book::class.java)
                        bookList.add(book!!)
                    }

                    val context = context
                    if (context !=null){
                        val adapter = BookAdapter(context, R.layout.books, bookList)
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
        val intent= Intent(context, AddBookActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}