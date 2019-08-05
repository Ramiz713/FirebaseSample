package com.itis2019.firebasesample.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.itis2019.firebasesample.R
import com.itis2019.firebasesample.utils.Helpers.Companion.validateEditText
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        firebaseAuth = FirebaseAuth.getInstance()

        initClickListeners()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) =
                Snackbar.make(findViewById(R.id.layout_phone_auth), "Code here!", Snackbar.LENGTH_SHORT).show()

            override fun onVerificationFailed(exception: FirebaseException) {
                progress_bar.visibility = View.GONE
                Snackbar.make(findViewById(R.id.layout_phone_auth), "${exception.message}", Snackbar.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                progress_bar.visibility = View.GONE
                ed_phone_number.isEnabled = false
                btn_start_verification.isEnabled = false
                btn_resend.isEnabled = true
                btn_verify_phone.isEnabled = true

                storedVerificationId = verificationId
                resendToken = token
            }
        }

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        progress_bar.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        progress_bar.visibility = View.VISIBLE
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
            phoneNumber: String,
            token: PhoneAuthProvider.ForceResendingToken?
    ) {
        progress_bar.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks,
                token)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    } else if (task.isCanceled || task.isComplete)
                        Snackbar.make(findViewById(R.id.layout_phone_auth), "${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                    progress_bar.visibility = View.GONE
                }
    }

    private fun initClickListeners() {

        btn_start_verification.setOnClickListener {
            val phoneNumber = ed_phone_number.text.toString()
            if (!validateEditText(ed_phone_number, phoneNumber))
                return@setOnClickListener
            startPhoneNumberVerification(phoneNumber)
        }

        btn_verify_phone.setOnClickListener {
            val code = ed_verification_code.text.toString()
            if (!validateEditText(ed_verification_code, code))
                return@setOnClickListener
            verifyPhoneNumberWithCode(storedVerificationId, code)
        }

        btn_resend.setOnClickListener {
            val phoneNumber = ed_phone_number.text.toString()
            resendVerificationCode(phoneNumber, resendToken)
        }
    }
}
