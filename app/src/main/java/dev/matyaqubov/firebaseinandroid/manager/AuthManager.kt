package dev.matyaqubov.firebaseinandroid.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthException




class AuthManager {
    companion object {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        fun isSignedIn(): Boolean {
            return firebaseUser != null
        }

        fun singIn(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handler.onSuccess()
                } else {

                    handler.onError(task.exception)

                }
            }
        }

        fun singUp(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handler.onSuccess()
                    } else {
                        handler.onError(task.exception)
                    }
                }
        }

        fun signOut(){
            firebaseAuth.signOut()
        }
    }
}