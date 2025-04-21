package com.example.sociallogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sociallogin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var googleUserEmail: String = ""
    var googleUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       /* if (intent.hasExtra("googleUserID")){
            googleUserID = intent.getStringExtra("googleUserID").toString()
            Log.d("GoogleLogin", "googleUserID..." + googleUserID)

        }

        if (intent.hasExtra("googleUserEmail")){
            googleUserEmail = intent.getStringExtra("googleUserEmail").toString()
            Log.d("GoogleLogin", "googleUserEmail..." + googleUserEmail)
        }  */

        binding.apply {

            btnLoginWithFB.setOnClickListener {
                startActivity(Intent(this@MainActivity, FBLoginActivity::class.java))
            }

            btnTwitter.setOnClickListener {
                startActivity(Intent(this@MainActivity, TwitterLoginActivity::class.java))
            }
        }

    }
}