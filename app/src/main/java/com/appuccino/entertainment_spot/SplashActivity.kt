package com.appuccino.entertainment_spot

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Launch splash screen, waits ~2 seconds via coroutine,
 * then starts MainActivity and finishes the splash so the user can’t navigate back to it.
 *
 **/

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spash_screen)

        // wait 3 seconds using coroutine
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // optionally finish so the user can't go back to splash
        }
    }
}
