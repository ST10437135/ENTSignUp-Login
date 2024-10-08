package com.example.etnthreepagesv2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.etnthreepagesv2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up click listener for the sign-up button
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailTxtInp.text.toString()
            val password = binding.passwordTxtInp.text.toString()

            // Check if email and password fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show the progress bar
                binding.progressBar.visibility = android.view.View.VISIBLE

                // Start sign-up process
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        // Hide the progress bar
                        binding.progressBar.visibility = android.view.View.GONE

                        if (task.isSuccessful) {
                            // Sign-up success, navigate to the home page
                            val intent = Intent(this, HomePageActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign-up fails, display a message to the user
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Notify the user to fill in all fields
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
