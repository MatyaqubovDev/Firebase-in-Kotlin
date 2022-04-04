package dev.matyaqubov.firebaseinandroid.manager

interface AuthHandler {
    fun onSuccess()
    fun onError(exception: Exception?)
}