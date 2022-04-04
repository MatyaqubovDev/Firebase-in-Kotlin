package dev.matyaqubov.firebaseinandroid.activity

import android.os.Bundle
import android.util.Log
import dev.matyaqubov.firebaseinandroid.databinding.ActivityCreateBinding
import dev.matyaqubov.firebaseinandroid.manager.DatabaseHandler
import dev.matyaqubov.firebaseinandroid.manager.DatabaseManager
import dev.matyaqubov.firebaseinandroid.model.Post

class CreateActivity : BaseActivity() {
    lateinit var binding: ActivityCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            bCreate.setOnClickListener {
                var title = etTitle.text.toString()
                var body = etBody.text.toString()
                val post = Post(title, body)
                storeDatabase(post)

            }
            ivClose.setOnClickListener {
                finish()
            }
        }
    }

    private fun storeDatabase(post: Post) {
        showLoading(this)
        DatabaseManager.storyPost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("@@@", "Post is saved ")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("@@@", "Post is not saved ")
            }

        })
    }

    private fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}