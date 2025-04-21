package com.example.sociallogin

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sociallogin.databinding.ActivityMainBinding
import com.example.sociallogin.databinding.ActivityTwitterLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider

class TwitterLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTwitterLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var provider: OAuthProvider.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTwitterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        provider = OAuthProvider.newBuilder("twitter.com")
        provider.addCustomParameter("lang", "en")

        val pending = auth.pendingAuthResult

        if (pending != null) {
            pending.addOnSuccessListener { handleSuccess(it.user) }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    println("TWITTER_LOGIN Error: ${it.message}")
                }
        }

        binding.btnTwitterLogin.setOnClickListener {
            auth
                .startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    handleSuccess(it.user)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    println("TWITTER_LOGIN Error: ${it.message}")
                }
        }
    }

    private fun handleSuccess(user: FirebaseUser?) {
        Toast.makeText(this, "Logged in as: ${user?.displayName}", Toast.LENGTH_SHORT).show()
        println("TWITTER_LOGIN Logged in as: ${user?.displayName}")
    }
}