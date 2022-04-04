package dev.matyaqubov.firebaseinandroid.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import dev.matyaqubov.firebaseinandroid.adapter.PostAdapter
import dev.matyaqubov.firebaseinandroid.databinding.ActivityMainBinding
import dev.matyaqubov.firebaseinandroid.manager.AuthManager
import dev.matyaqubov.firebaseinandroid.manager.DatabaseHandler
import dev.matyaqubov.firebaseinandroid.manager.DatabaseManager
import dev.matyaqubov.firebaseinandroid.model.Post
import dev.matyaqubov.firebaseinandroid.utils.Extentions.toast

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    var postList = ArrayList<Post>()
    var adapter = PostAdapter(this, postList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBinding()

    }

    private fun initBinding() {
        binding.recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 1)
        binding.recyclerView.adapter = adapter
        binding.swipeRefreshLayout.isRefreshing=false
        binding.fabCreate.setOnClickListener {
            callCreateActivity()
        }
        binding.ivLogout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(context)

        }
        apiLoadPosts()
    }


    private fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                postList.clear()
                postList.addAll(posts)
                //refreshAdapter(posts)
                adapter.notifyDataSetChanged()
                dismissLoading()
            }

            override fun onError() {
                toast("I don't know")
                dismissLoading()
            }

        })
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {

    }

    fun callCreateActivity() {
        val intent = Intent(context, CreateActivity::class.java)
        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Load all posts...
                apiLoadPosts()
            }
        }
}