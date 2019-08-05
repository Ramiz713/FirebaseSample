package com.itis2019.firebasesample.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.itis2019.firebasesample.R
import com.itis2019.firebasesample.utils.Helpers.Companion.validateEditText
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val firebaseAuth = FirebaseAuth.getInstance()

        btn_send_pass.setOnClickListener {
            val email = ed_email.text.toString()

            if (validateEditText(ed_email, email))
                return@setOnClickListener

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful)
                    finish()
                else Snackbar.make(
                    findViewById(R.id.layout_forgot_pass),
                    "${task.exception?.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
