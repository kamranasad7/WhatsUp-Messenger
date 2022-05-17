package com.ll.whatsup.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if (Firebase.auth.currentUser != null) {
            Toast.makeText(this, "User already logged in.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

        findViewById<Button>(R.id.welcome_continue_btn).setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            resultLauncher.launch(i)
        }
    }
}