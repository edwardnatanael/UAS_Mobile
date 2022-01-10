package com.capstone.project.akselerasi_vaksinasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.capstone.project.akselerasi_vaksinasi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var visiblePass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.visibility = View.INVISIBLE

        binding.eyeButton.setOnClickListener {
            if(visiblePass == false){
                visiblePass = true
                binding.textPassword.transformationMethod = null
            } else {
                visiblePass = false
                binding.textPassword.transformationMethod = PasswordTransformationMethod()
            }
        }

        hideKeyboard()
        login()
    }

    private fun hideKeyboard() {
        binding.layoutLogin.setOnClickListener {
            val view = this.currentFocus
            if(view != null) {
                val hide = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun login(){

        binding.btnLogin.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()

            if(binding.textEmail.text.toString().isEmpty()){
                binding.progressBar.visibility = View.INVISIBLE
                binding.textEmail.setError("Field ini harus diisi!!")
            } else if(binding.textPassword.text.toString().isEmpty()){
                binding.progressBar.visibility = View.INVISIBLE
                binding.textPassword.setError("Field ini harus diisi!!")
            } else {
                auth = Firebase.auth
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.INVISIBLE
                            // Sign in success, update UI with the signed-in user's information
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            binding.progressBar.visibility = View.INVISIBLE
                            // If sign in fails, display a message to the user.
                            Log.w("sign in", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed. Try again!",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }


        }
    }
}