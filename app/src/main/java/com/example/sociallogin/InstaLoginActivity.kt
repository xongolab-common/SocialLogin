package com.example.sociallogin

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sociallogin.databinding.ActivityInstaLoginBinding
import com.example.sociallogin.databinding.ActivityTwitterLoginBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import androidx.core.net.toUri

class InstaLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstaLoginBinding

    private val clientId = "580805414379848"
    private val clientSecret = "1214c1be775293adee6e55aca8b96582"
    private val redirectUri = "https://social-login-95614.firebaseapp.com/__/auth/handler"

    private val authUrl = "https://api.instagram.com/oauth/authorize" +
            "?client_id=$clientId" +
            "&redirect_uri=$redirectUri" +
            "&scope=user_profile,user_media" +
            "&response_type=code"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInstaLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith(redirectUri)) {
                    val uri = url.toUri()
                    val code = uri.getQueryParameter("code")
                    code?.let {
                        getAccessToken(it)
                    }
                    return true
                }
                return false
            }
        }

        binding.webView.loadUrl(authUrl)
    }

    private fun getAccessToken(code: String) {
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", redirectUri)
            .add("code", code)
            .build()

        val request = Request.Builder()
            .url("https://api.instagram.com/oauth/access_token")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("InstagramLogin Access token request failed" + e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonObject = JSONObject(responseBody)
                    val accessToken = jsonObject.getString("access_token")
                    val userId = jsonObject.getJSONObject("user").getString("id")

                    // Call Graph API to get user info
                    getUserProfile(accessToken)
                }
            }
        })
    }

    private fun getUserProfile(accessToken: String) {
        val url = "https://graph.instagram.com/me?fields=id,username&access_token=$accessToken"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("InstagramLogin User profile fetch failed" + e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val userJson = JSONObject(responseBody)
                    val username = userJson.getString("username")
                    val id = userJson.getString("id")

                    runOnUiThread {
                        Toast.makeText(
                            this@InstaLoginActivity,
                            "Welcome, $username!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }
}