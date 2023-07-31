package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.mobicam.databinding.ActivityLoginBinding
import com.android.mobicam.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textRegisterHere.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.textForgotPass.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.etLoginEmail.text.toString()
            val pass = binding.etLoginPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else{
                Toast.makeText(this, "Please fill empty fields !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}