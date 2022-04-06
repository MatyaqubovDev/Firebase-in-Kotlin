package dev.matyaqubov.firebaseinandroid.manager

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class StorageManager {

    companion object {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference()
        val photoRef = storageRef.child("photos")

        fun uploadPhotos(uri: Uri, handler: StorageHandler) {
            val fileName = System.currentTimeMillis().toString() + ".png"
            val uploadTask: UploadTask = photoRef.child(fileName).putFile(uri)
            uploadTask.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val imgUrl = it.toString()
                    handler.onSuccess(imgUrl)
                }.addOnFailureListener { e ->
                    handler.onError(e)
                }
            }.addOnFailureListener { e ->
                handler.onError(e)
            }
        }
    }
}