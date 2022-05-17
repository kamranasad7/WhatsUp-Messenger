package com.ll.whatsup.activities

import android.app.Activity
import android.content.Intent
import java.util.concurrent.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.R

class SignUpActivity : AppCompatActivity() {

    lateinit var phoneText: EditText
    lateinit var signUpBtn: Button
    lateinit var otpText: EditText
    lateinit var verifyBtn: Button

    private lateinit var auth: FirebaseAuth
    private var verificationID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        phoneText = findViewById(R.id.signup_phone_num)
        signUpBtn = findViewById(R.id.signup_next_btn)
        otpText = findViewById(R.id.otp_text)
        verifyBtn = findViewById(R.id.signup_verify_btn)
        auth = Firebase.auth

        phoneText.doAfterTextChanged { signUpBtn.isEnabled = it.toString().isNotBlank() }

        signUpBtn.setOnClickListener {
            sendVerificationCode(phoneText.text.toString())
        }
        verifyBtn.setOnClickListener { verifyCode(otpText.text.toString()) }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            if(credential.smsCode != null){
                verifyCode(credential.smsCode!!);
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(this@SignUpActivity, "Verification Failed: Invalid Request", Toast.LENGTH_LONG).show()
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(this@SignUpActivity, "Verification Failed: SMS Quota exceeded", Toast.LENGTH_LONG).show()
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            verificationID = verificationId

            Toast.makeText(applicationContext, "OTP sent. ", Toast.LENGTH_SHORT).show()
            otpText.isEnabled = true
            verifyBtn.isEnabled = true
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(smsCode: String) {
        val credentials = PhoneAuthProvider.getCredential(verificationID, smsCode)
        signInByCredentials(credentials)
    }

    private fun signInByCredentials(credentials: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "SignIn Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CreateProfileActivity::class.java))
                setResult(Activity.RESULT_OK)
                finish()
            }
            else{
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(applicationContext, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}