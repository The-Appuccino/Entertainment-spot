package com.appuccino.entertainment_spot

import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.FirebaseUser

/**
 * Launch splash screen, waits ~2 seconds via coroutine,
 * then starts MainActivity and finishes the splash so the user can’t navigate back to it.
 *
 **/

class StartScreenActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    private var navigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_screen)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth

    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
        if (currentUser != null) {
            Log.d(TAG, "Already signed in. uid=${currentUser?.uid}, anon=${currentUser?.isAnonymous}")
            goToMainActivityScreen()
        } else {
            Log.d(TAG, "No user yet. Signing in anonymously…")
            signInAnonymously()
        }

    }

    private fun signInAnonymously() {
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success uid=${user?.uid}")
                    goToMainActivityScreen()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    // wait 3 seconds using coroutine
    private fun goToMainActivityScreen() {
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@StartScreenActivity, MainActivity::class.java))
            finish() // optionally finish so the user can't go back to splash
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    companion object {
        private const val TAG = "AnonymousAuth"
    }

}
