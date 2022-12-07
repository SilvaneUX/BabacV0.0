package com.smart.smartbalibackpaker.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.model.User
import com.smart.smartbalibackpaker.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finishAffinity()
        }
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val cPassword = binding.etCpassword.text.toString().trim()

            if(email.isEmpty()){
                binding.etEmail.apply {
                    error = "Email Harus Diisi!"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.apply {
                    error = "Email Tidak Valid!"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (username.isEmpty()){
                binding.etUsername.apply {
                    error = "Username harus diisi"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                binding.etPassword.apply {
                    error = "Password harus lebih dari 6 karakter"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (password != cPassword){
                binding.etCpassword.apply {
                    error = "Password harus sama"
                    requestFocus()
                }
                return@setOnClickListener
            }

            val user = User(
                username,
                email,
                password,
            )

            val intent = Intent(this, AddPhotoActivity::class.java)
            intent.putExtra(AddPhotoActivity.USER, user)
            startActivity(intent)
        }

        setupToolbar()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            Intent(this@SignUpActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tbSignUp.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.tbSignUp.setNavigationOnClickListener {
            finish()
        }
    }
}