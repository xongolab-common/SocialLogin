package com.example.sociallogin

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // for Twitter Login
        FirebaseApp.initializeApp(this)

        // for FB Login
        FacebookSdk.setApplicationId("580805414379848")
        FacebookSdk.setClientToken("25c5bdf21e111d9705568f5874fe6896")
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // for Twitter Login
     /*   val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    "YOUR_CONSUMER_KEY", // from Twitter dev portal
                    "YOUR_CONSUMER_SECRET"
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)  */
    }
}

