package com.smart.smartbalibackpaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.databinding.ActivityChangePassBinding
import java.util.*

class ChangePassActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private val binding by lazy{ActivityChangePassBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        val user = auth.currentUser

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnChangePassword.setOnClickListener {
            val password = binding.etPassword.text.toString().trim()
            val cPassword = binding.etCpassword.text.toString().trim()

            if (password.isEmpty() || password.length < 6) {
                binding.etPassword.apply {
                    error = "Password harus lebih dari 6 karakter"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (password != cPassword) {
                binding.etCpassword.apply {
                    error = "Password harus sama"
                    requestFocus()
                }
                return@setOnClickListener
            }

            user?.let {
                it.updatePassword(password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Password has been changed", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Failed to change password, please try again later", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}