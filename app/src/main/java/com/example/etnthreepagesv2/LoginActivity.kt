package com.example.etnthreepagesv2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.etnthreepagesv2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set click listener for login button
        binding.loginBtn.setOnClickListener {
            val email = binding.emailTxtInp.text.toString().trim()
            val password = binding.passwordTxtInp.text.toString().trim()

            // Validate inputs
            if (!isValidEmail(email)) {
                binding.emailTxtInp.error = "Please enter a valid email"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.passwordTxtInp.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            // Show progress bar and disable button to prevent multiple clicks
            binding.progressBar.visibility = View.VISIBLE
            binding.loginBtn.isEnabled = false

            // Sign in with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Hide progress bar
                    binding.progressBar.visibility = View.GONE
                    binding.loginBtn.isEnabled = true

                    if (task.isSuccessful) {
                        // Login success, navigate to home page
                        val intent = Intent(this, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Login failed, show appropriate error message
                        when {
                            task.exception?.message?.contains("no user record") == true -> {
                                showMessage("No account found with this email")
                            }
                            task.exception?.message?.contains("password is invalid") == true -> {
                                showMessage("Invalid password")
                            }
                            else -> {
                                showMessage("Authentication failed: ${task.exception?.message}")
                            }
                        }
                    }
                }
        }
    }

    // Helper function to validate email
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Helper function to show a toast message
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
