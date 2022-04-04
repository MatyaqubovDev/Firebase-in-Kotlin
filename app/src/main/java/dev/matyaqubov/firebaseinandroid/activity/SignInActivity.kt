package dev.matyaqubov.firebaseinandroid.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dev.matyaqubov.firebaseinandroid.R
import dev.matyaqubov.firebaseinandroid.manager.AuthHandler
import dev.matyaqubov.firebaseinandroid.manager.AuthManager
import dev.matyaqubov.firebaseinandroid.utils.Extentions.toast

class SignInActivity : BaseActivity() {
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var b_signIn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        b_signIn = findViewById(R.id.btn_signin)
        b_signIn.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
//            if (isEmailValid(email)) firebaseSignIn(email, password)
//            else toast("Siz email deb kiritgan string Email emas")
            firebaseSignIn(email, password)
        }
        val tv_signUp = findViewById<TextView>(R.id.tv_signup)
        tv_signUp.setOnClickListener {
            callSingUpActivity()
        }

    }

    private fun callSingUpActivity() {
        val intent = Intent(context, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.singIn(email, password, object : AuthHandler {
            override fun onSuccess() {
                dismissLoading()
                toast("Signed in Successfully")
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                var errorCode=(exception as FirebaseAuthException).errorCode
                when(errorCode){
                    "ERROR_INVALID_EMAIL" -> {
                        toast("The email address is badly formatted.")
                        et_email.setError("The email address is badly formatted.")
                        et_email.requestFocus()
                    }
                    "ERROR_WRONG_PASSWORD" ->{
                        toast("The password is invalid or the user does not have a password.")
                        et_password.setError("password is incorrect")
                        et_password.requestFocus()
                        et_password.setText("")
                    }
                }

                dismissLoading()
                toast("Sign in Failed")
            }

        })
    }
}