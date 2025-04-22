package com.example.sociallogin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sociallogin.databinding.ActivityTwitterLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class TwitterLoginActivity : AppCompatActivity() {

    private var twitterBearerToken = "AAAAAAAAAAAAAAAAAAAAAP0M0wEAAAAAaIffze4%2FsmVMvUr2iZv2Jmp%2FEL8%3DDrAKoyc9sTbBBfsjULJtCS3S9x2TePO5jwBxUQeNi9Pc05Undj"
    private var twitterAccessToken = "1914272702506123264-hBE29g33maZdI5dyegzW1k9j6E37qk"
    private var twitterAccessTokenSecret = "d75kKODAJaVZJPQnnqfnGCwgd50zpaNtzp8ZjI5Odoytq"

    private var twitterAPIKey = "vDFFBYBvgaBJVY4tDhVXNa9n4"
    private var twitterAPIKeySecret = "rX9IJNUSi5tlKnI35E9FUDeKp0EqBaj91lKxSn1W9k9DcG5mUM"

    private var twitterCallbackURLFirebase = "https://social-login-95614.firebaseapp.com/__/auth/handler"

    private lateinit var binding: ActivityTwitterLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var isSigningIn = false // ✅ new flag


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTwitterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.twitterLoginButton.setOnClickListener {
            if (!isSigningIn) {
                signInWithTwitter()
            }
        }

    }

    private fun signInWithTwitter() {
        isSigningIn = true // ✅ prevent repeated login attempts

        val provider = OAuthProvider.newBuilder("twitter.com")

        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    showToast("Signed in as ${it.user?.displayName}")
                    println("TWITTER_LOGIN Signed in as ${it.user?.displayName}")
                    isSigningIn = false
                }
                .addOnFailureListener {
                    showToast("Error: ${it.message}")
                    println("TWITTER_LOGIN Error: ${it.message}")
                    isSigningIn = false
                }
        } else {
            firebaseAuth
                .startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    showToast("Signed in as ${it.user?.displayName}")
                    println("TWITTER_LOGIN Signed in as ${it.user?.displayName}")
                    isSigningIn = false
                }
                .addOnFailureListener {
                    showToast("Error: ${it.message}")
                    println("TWITTER_LOGIN Error: ${it.message}")
                    isSigningIn = false
                }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}