package dev.matyaqubov.firebaseinandroid.manager

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.matyaqubov.firebaseinandroid.model.Post

class DatabaseManager {
    companion object {
        private var database = FirebaseDatabase.getInstance().reference
        private var reference = database.child("posts")

        fun storyPost(post: Post, handler: DatabaseHandler) {
            val key = reference.push().key
            if (key == null) return
            post.id = key
            reference.child(key).setValue(post)
                .addOnSuccessListener {
                    handler.onSuccess()
                }.addOnFailureListener {
                    handler.onError()
                }
        }

        fun apiUpdatePost(post: Post,handler: DatabaseHandler){
            val map = HashMap<String,Any>()
            map.put("title",post.title!!)
            map.put("body",post.body!!)
            reference.child(post.id!!).updateChildren(map).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    handler.onSuccess(post)
                }else{
                    handler.onError()
                }
            }
        }

        fun apiDeletePost(post: Post,handler: DatabaseHandler){
            reference.child(post.id!!).removeValue().addOnCompleteListener {
                if(it.isSuccessful){
                    handler.onSuccess()
                }else{
                    handler.onError()
                }
            }
        }

        fun apiLoadPosts(handler: DatabaseHandler) {
            reference.addValueEventListener(object : ValueEventListener {
                var posts = ArrayList<Post>()
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (datasnapshot.exists()) {
                        for (snapshot in datasnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            post.let {
                                posts.add(post!!)
                            }
                        }
                        handler.onSuccess(posts = posts)
                    } else {
                        handler.onSuccess(posts = ArrayList())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    handler.onError()
                }

            })
        }


    }

}