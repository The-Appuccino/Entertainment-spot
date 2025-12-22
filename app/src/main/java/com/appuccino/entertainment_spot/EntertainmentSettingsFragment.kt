package com.appuccino.entertainment_spot

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class EntertainmentSettingsFragment: Fragment(R.layout.fragment_entertainment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "Not signed in"
        view.findViewById<TextView>(R.id.sessionIDNumber).text = uid
    }
}