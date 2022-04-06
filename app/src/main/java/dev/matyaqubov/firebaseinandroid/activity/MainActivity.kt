package dev.matyaqubov.firebaseinandroid.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import dev.matyaqubov.firebaseinandroid.adapter.PostAdapter
import dev.matyaqubov.firebaseinandroid.databinding.ActivityMainBinding
import dev.matyaqubov.firebaseinandroid.manager.AuthManager
import dev.matyaqubov.firebaseinandroid.manager.DatabaseHandler
import dev.matyaqubov.firebaseinandroid.manager.DatabaseManager
import dev.matyaqubov.firebaseinandroid.model.Post
import dev.matyaqubov.firebaseinandroid.utils.Extentions.toast
import android.R
import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

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
        binding.swipeRefreshLayout.isRefreshing = false
        binding.fabCreate.setOnClickListener {
            callCreateActivity()
        }
        binding.ivLogout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(context)

        }
        apiLoadPosts()
        setItemSwipeListener()
        adapter.onUpdateClick = {
            callUpdateActivity(it)
        }
        adapter.onDeleteClick = {
            apiDeletePost(it)
        }
    }

    private fun callUpdateActivity(post: Post) {
        val intent = Intent(context, UpdateActivity::class.java)
        intent.putExtra("post",post)
        resultLauncher.launch(intent)
    }

    private fun setItemSwipeListener() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val limitScrollX = dpToPx(
                100f,
                this@MainActivity
            ) // todo, the limit of swipe, same as the delete button in item, 100dp
            private var currentScrollX = 0
            private var currentScrollWhenInActive = 0
            private var initWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }


            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                    }

                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                        //slide with auto anim
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollWhenInActive = viewHolder.itemView.scrollX
                            initWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollWhenInActive * dX / initWhenInActive).toInt(),
                                0
                            )
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun apiDeletePost(post: Post){
        showLoading(this)
        DatabaseManager.apiDeletePost(post,object : DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()
                apiLoadPosts()
            }

            override fun onError() {
                dismissLoading()
            }

        })
    }
    private fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                postList.clear()
                postList.addAll(posts)
                adapter.notifyDataSetChanged()
                dismissLoading()
            }

            override fun onError() {
                toast("I don't know")
                dismissLoading()
            }

        })
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

    private fun dpToPx(dpValue: Float, context: Context): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }
}