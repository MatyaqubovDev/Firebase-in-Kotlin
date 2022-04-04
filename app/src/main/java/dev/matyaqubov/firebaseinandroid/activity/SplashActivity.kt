package dev.matyaqubov.firebaseinandroid.activity


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import dev.matyaqubov.firebaseinandroid.R
import dev.matyaqubov.firebaseinandroid.manager.AuthManager


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )
        initViews()
    }

    private fun initViews() {
        countDownTimer()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {
                //har bir qadamni ko'rish
            }
            override fun onFinish() {
                if (AuthManager.isSignedIn()){
                    callMainActivity(this@SplashActivity)
                } else{
                    callSignInActivity(this@SplashActivity)
                }
            }
        }.start()
    }

}