package dev.matyaqubov.firebaseinandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.matyaqubov.firebaseinandroid.R
import dev.matyaqubov.firebaseinandroid.model.Post

class PostAdapter(var context: Context, var posts: ArrayList<Post>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        if (holder is PostViewHolder) {
            holder.apply {
                title.setText(post.title)
                body.setText(post.body)
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class PostViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var title = view.findViewById<TextView>(R.id.tv_title)
        var body = view.findViewById<TextView>(R.id.tv_body)


    }
}