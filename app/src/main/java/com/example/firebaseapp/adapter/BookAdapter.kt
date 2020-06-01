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
import com.example.firebaseapp.model.Book
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class BookAdapter(val mCtx: Context, val layoutResId:Int, val bookList:List<Book>)
    : ArrayAdapter<Book>(mCtx,layoutResId,bookList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitleRow = view.findViewById<TextView>(R.id.textViewTitle_row)
        val textViewAuthorRow = view.findViewById<TextView>(R.id.textViewAuthor_row)
        val textViewYearRow = view.findViewById<TextView>(R.id.textViewYear_row)
        val ratingBarRow = view.findViewById<RatingBar>(R.id.ratinBar_row)
        val readImageButtonRow = view.findViewById<ImageButton>(R.id.readImageButton_row)
        val textViewUpdateRow = view.findViewById<TextView>(R.id.textViewUpdate_row)
        val deleteButtonRow=view.findViewById<ImageButton>(R.id.deleteButton_row)

        val book = bookList[position]
        textViewTitleRow.text =book.title
        textViewAuthorRow.text=book.author
        textViewYearRow.text=book.year
        ratingBarRow.rating=book.rating.toFloat()

        if(!book.read){
            readImageButtonRow.setImageResource(R.drawable.baseline_visibility_off_black_24dp)
            ratingBarRow.isVisible=false
        }

        textViewUpdateRow.setOnClickListener {
            showUpdateDialog(book)
        }

        deleteButtonRow.setOnClickListener {
            deleteBook(book)
        }

        return view
    }

    private fun deleteBook(book: Book) {
        val title:String=book.title
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are You Sure to Delete: $title ?")
            .setPositiveButton("Yes") { dialog, which ->
                val dbBook = FirebaseDatabase.getInstance().getReference("books").child(book.id)
                dbBook.removeValue()
                Toast.makeText(context,"$title Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
            .show()

    }

    private fun showUpdateDialog(book: Book) {

        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update Book")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_book, null)

        val editTextTitleUpdate= view.findViewById<TextInputEditText>(R.id.editTextTitleUpdate)
        val textInputLayoutTitleUpdate = view.findViewById(R.id.text_input_layout_update_title) as TextInputLayout

        val editTextAuthorUpdate= view.findViewById<TextInputEditText>(R.id.editTextAuthorUpdate)
        val textInputLayoutAuthorUpdate = view.findViewById(R.id.text_input_layout_update_author) as TextInputLayout
        val editTextYearUpdate = view.findViewById<TextInputEditText>(R.id.editTextUpdateYear)
        val textInputLayoutYearUpdate = view.findViewById(R.id.text_input_layout_update_year) as TextInputLayout

        val readSwitch = view.findViewById<Switch>(R.id.readSwitchUpdate)

        val ratingBarUpdate = view.findViewById<RatingBar>(R.id.ratingBarUpdate)



        editTextTitleUpdate.setText(book.title)
        editTextAuthorUpdate.setText(book.author)
        editTextYearUpdate.setText(book.year)
        readSwitch.isChecked = book.read
        ratingBarUpdate.rating = book.rating.toFloat()

        if(!readSwitch.isChecked){
            ratingBarUpdate.rating = 0f
            ratingBarUpdate.isEnabled = false
        }

        readSwitch.setOnClickListener {
            ratingBarUpdate.isEnabled = readSwitch.isChecked
            if(!readSwitch.isChecked){
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

                val dbBook = FirebaseDatabase.getInstance().getReference("books")
                val title = editTextTitleUpdate.text.toString().trim()
                val author = editTextAuthorUpdate.text.toString().trim()
                val year = editTextYearUpdate.text.toString().trim()

                if(title.isEmpty() || author.isEmpty() || year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888) {
                    if(title.isEmpty()){
                        textInputLayoutTitleUpdate.error = "Please enter title"
                    }else{
                        textInputLayoutTitleUpdate.error = null
                    }
                    if(author.isEmpty()){
                        textInputLayoutAuthorUpdate.error = "Please enter author"
                    }else{
                        textInputLayoutAuthorUpdate.error = null
                    }
                    if(year.isEmpty() || year.toInt() > 2020 || year.toInt() < 1888){
                        textInputLayoutYearUpdate.error = "Wrong Year"
                    }else{
                        textInputLayoutYearUpdate.error = null
                    }
                } else {
                    val book2 = Book(book.id, title, author,year,readSwitch.isChecked,ratingBarUpdate.rating.toInt())

                    dbBook.child(book.id).setValue(book2)

                    Toast.makeText(mCtx, "Book updated", Toast.LENGTH_LONG).show()

                    dialog.dismiss()
                }
            }
    }
}