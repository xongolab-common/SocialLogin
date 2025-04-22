package com.example.sociallogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sociallogin.databinding.ActivityFbloginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import org.json.JSONException


class FBLoginActivity : AppCompatActivity() {

    // valid OAuth Redirect URL :
    // https://social-login-95614.firebaseapp.com/__/auth/handler
    // Client Token : 25c5bdf21e111d9705568f5874fe6896

    // for Logout : LoginManager.getInstance().logOut()

    private lateinit var binding: ActivityFbloginBinding
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFbloginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        binding.loginButton.setPermissions("email", "public_profile")

        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                val userId = accessToken.userId
                Log.d("FacebookLogin", "Success: $userId")
                getUserProfile(accessToken)
            }

            override fun onCancel() {
                Log.d("FacebookLogin", "Cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookLogin", "Error: ${error.message}")
            }
        })
    }


    private fun getUserProfile(token: AccessToken) {
        val request = GraphRequest.newMeRequest(token) { obj, _ ->
            try {
                val name = obj?.getString("name")
                val email = obj?.getString("email")
                Log.d("FacebookProfile", "Name: $name, Email: $email")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
