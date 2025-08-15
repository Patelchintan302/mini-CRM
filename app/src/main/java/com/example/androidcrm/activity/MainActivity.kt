package com.example.androidcrm.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrm.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val customerButton = findViewById<Button>(R.id.CustomerButton)
        val orderButton = findViewById<Button>(R.id.OrderButton)
        val signOutButton = findViewById<Button>(R.id.SignOutButton)

        customerButton.setOnClickListener {
            startActivity(Intent(this, CustomerActivity::class.java))
        }

        orderButton.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }

        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}