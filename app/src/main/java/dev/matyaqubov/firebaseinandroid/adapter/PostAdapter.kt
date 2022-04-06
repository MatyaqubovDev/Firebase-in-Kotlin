package dev.matyaqubov.firebaseinandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.matyaqubov.firebaseinandroid.R
import dev.matyaqubov.firebaseinandroid.databinding.ItemPostListWithMenuBinding
import dev.matyaqubov.firebaseinandroid.model.Post
import java.lang.ref.WeakReference

class PostAdapter(var context: Context,var list: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var onDeleteClick: ((post :Post) -> Unit)
    lateinit var onUpdateClick: ((post :Post) -> Unit)

    //private var dif = AsyncListDiffer(this, ITEM_DIFF)

    inner class ViewHolder(val binding: ItemPostListWithMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        private val view = WeakReference(binding.root)
        fun bind() {
            val card = list[adapterPosition]
            view.get()?.let {
                it.setOnClickListener {
                    if (view.get()?.scrollX != 0) {
                        view.get()?.scrollTo(0, 0)
                    }
                }
            }
            binding.apply {
                tvTitle.text = card.title
                tvBody.text=card.body
                ivDelete.setOnClickListener {
                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                    onDeleteClick.invoke(card)
                }
                ivEdit.setOnClickListener {
                    Toast.makeText(context, "update", Toast.LENGTH_SHORT).show()
                    onUpdateClick.invoke(card)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPostListWithMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as ViewHolder).bind()

    override fun getItemCount(): Int = list.size

//    fun submitList(list: List<Post>) {
//        dif.submitList(list)
//    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.title == newItem.title
        }
    }
}