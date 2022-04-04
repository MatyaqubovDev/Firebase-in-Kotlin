package dev.matyaqubov.firebaseinandroid.manager

import dev.matyaqubov.firebaseinandroid.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post?=null,posts:ArrayList<Post> = ArrayList())
    fun onError()
}