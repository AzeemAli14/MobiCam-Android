package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    private lateinit var image:ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        image = findViewById(R.id.app_logo);
        image.alpha = 0f;
        image.animate().setDuration(4000).alpha(1f).withEndAction {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish();
        }
    }
}