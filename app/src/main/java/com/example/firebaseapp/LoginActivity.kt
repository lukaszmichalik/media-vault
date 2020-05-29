package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button_haveAccount.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        buttonLogin.setOnClickListener {
            doLogin()
        }

    }
    private fun doLogin(){
        if(login_username.text.toString().isEmpty()){
            login_username.error = "Please enter email"
            login_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(login_username.text.toString()).matches()){
            login_username.error = "Please enter valid email"
            login_username.requestFocus()
            return
        }

        if (login_password.text.toString().isEmpty()){
            login_password.error = "Please enter password"
            login_password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(login_username.text.toString(),login_password.text.toString())
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    val user=auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)
                }
            }
    }

    public override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser!=null){
            if (currentUser.isEmailVerified){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext,"Please verify your email adress",Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(baseContext,"Login failed.",Toast.LENGTH_SHORT).show()
        }
    }
}



