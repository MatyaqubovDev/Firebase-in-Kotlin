package dev.matyaqubov.firebaseinandroid.utils

import android.app.Activity
import android.widget.Toast

object Extentions {
    fun Activity.toast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}