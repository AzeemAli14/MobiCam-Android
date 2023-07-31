package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {
    private lateinit var et_forgot: EditText
    private lateinit var btn_rest: Button
    private lateinit var btn_cancel: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        et_forgot = findViewById(R.id.et_forgot_email)
        btn_rest = findViewById(R.id.btn_reset)
        btn_cancel = findViewById(R.id.btn_cancel)

        auth = FirebaseAuth.getInstance()

        btn_rest.setOnClickListener {
            val pass = et_forgot.text.toString()
            auth.sendPasswordResetEmail(pass)
                .addOnSuccessListener {
                    Toast.makeText(this, "Please check your email!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        btn_cancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}