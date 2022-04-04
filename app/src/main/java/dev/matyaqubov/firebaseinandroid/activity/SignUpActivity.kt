package dev.matyaqubov.firebaseinandroid.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuthException
import dev.matyaqubov.firebaseinandroid.R
import dev.matyaqubov.firebaseinandroid.manager.AuthHandler
import dev.matyaqubov.firebaseinandroid.manager.AuthManager
import dev.matyaqubov.firebaseinandroid.utils.Extentions.toast

class SignUpActivity : BaseActivity() {
    lateinit var edt_email: EditText
    lateinit var edt_pwd:EditText
    lateinit var edt_confirm:EditText
    lateinit var edt_name:EditText
    lateinit var tv_signup: TextView
    lateinit var btn_signup: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    fun initViews() {
        edt_confirm = findViewById(R.id.et_confirm_password)
        edt_name = findViewById(R.id.et_fullname)
        edt_email = findViewById(R.id.et_email)
        tv_signup = findViewById(R.id.tv_signin)
        edt_pwd = findViewById(R.id.et_password)
        btn_signup = findViewById(R.id.btn_signup)

        btn_signup.setOnClickListener {
            val email = edt_email.text.toString().trim()
            val password = edt_pwd.text.toString().trim()
            if (isEmailValid(email)) firebaseSignUp(email,password)
            else toast("email yoki parol  xatokuuuuu")

        }

        tv_signup.setOnClickListener {
            callSignInActivity()
        }
    }

    private fun firebaseSignUp(email: String, password: String) {
        showLoading(this)
        AuthManager.singUp(email,password,object :AuthHandler{
            override fun onSuccess() {
                dismissLoading()
                toast("Signed up successfully")
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                var errorCode=(exception as FirebaseAuthException).errorCode
                if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                    toast("This credential is already associated with a different user account.")

                }
                dismissLoading()
                toast("Sign up failed")
            }

        })
    }

    fun callSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}