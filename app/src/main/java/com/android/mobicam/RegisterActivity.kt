package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.mobicam.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textLoginHere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener{
            val name = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val pass = binding.etRegisterPass.text.toString()
            val confirmPass = binding.etRegisterConpass.text.toString()
            val phoneNumber = binding.etRegisterPhone.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            val userMap = hashMapOf(
                                "Name" to name,
                                "Email" to email,
                                "Password" to pass,
                                "Phone Number" to phoneNumber
                            )
                            val userID = firebaseAuth.currentUser!!.uid
                            db.collection("userData").document(userID).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully Added !", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show()
                                }

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString() ,Toast.LENGTH_SHORT).show()
                        }
                    }

                } else{
                    Toast.makeText(this, "Password is not matching",Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Please fill empty fields !!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}