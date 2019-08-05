package com.itis2019.firebasesample.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.itis2019.firebasesample.R
import com.itis2019.firebasesample.utils.Helpers.Companion.validateEditText
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        firebaseAuth = FirebaseAuth.getInstance()
        initClickListeners()
    }

    private fun initClickListeners() {
        btn_create_account.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_sign_in_phone.setOnClickListener {
            startActivity(Intent(this, PhoneAuthActivity::class.java))
        }

        btn_forgot_pass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btn_sign_in_email.setOnClickListener { signIn() }
    }

    private fun signIn() {

        val email = ed_email.text.toString()
        val password = ed_pass.text.toString()

        if (!validateEditText(ed_email, email) || !validateEditText(ed_pass, password))
            return

        progress_bar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    } else Snackbar.make(
                            findViewById(R.id.layout_sign_in),
                            "Authentication failed. ${task.exception?.message}",
                            Snackbar.LENGTH_LONG
                    ).show()

                    progress_bar.visibility = View.GONE
                }
    }
}
