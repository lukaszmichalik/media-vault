package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        buttonRegister.setOnClickListener {
            signUpUser()
        }
    }
  private fun signUpUser(){
      if(username.text.toString().isEmpty()){
          username.error = "Please enter email"
          username.requestFocus()
          return
      }

      if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
          username.error = "Please enter valid email"
          username.requestFocus()
          return
      }

      if (password.text.toString().isEmpty()){
          password.error = "Please enter password"
          password.requestFocus()
          return
      }

      auth.createUserWithEmailAndPassword(username.text.toString(),password.text.toString())
          .addOnCompleteListener(this){task ->
              if (task.isSuccessful){
                  val user=auth.currentUser
                  user?.sendEmailVerification()
                      ?.addOnCompleteListener { task ->
                          if(task.isSuccessful){
                              startActivity(Intent(this,LoginActivity::class.java))
                              finish()
                          }
                      }

              } else{
                  Toast.makeText(baseContext,"sign up failed",
                  Toast.LENGTH_SHORT).show()
              }
          }
  }
}
