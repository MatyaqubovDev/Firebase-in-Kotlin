package dev.matyaqubov.firebaseinandroid.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dev.matyaqubov.firebaseinandroid.databinding.ActivityCreateBinding
import dev.matyaqubov.firebaseinandroid.manager.DatabaseHandler
import dev.matyaqubov.firebaseinandroid.manager.DatabaseManager
import dev.matyaqubov.firebaseinandroid.manager.StorageHandler
import dev.matyaqubov.firebaseinandroid.manager.StorageManager
import dev.matyaqubov.firebaseinandroid.model.Post
import java.lang.Exception

class CreateActivity : BaseActivity() {
    lateinit var binding: ActivityCreateBinding
    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            bCreate.setOnClickListener {
                var title = etTitle.text.toString()
                var body = etBody.text.toString()
                val post = Post(title, body)
                storePost(post)

            }
            ivClose.setOnClickListener {
                finish()
            }
            ivCamera.setOnClickListener {
                pickUserPhoto()
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

    fun storePost(post: Post) {
        if (pickedPhoto != null) {
            storeStorage(post)
        } else {
            storeDatabase(post)
        }
    }

    private fun storeStorage(post: Post) {
        showLoading(this)
        StorageManager.uploadPhotos(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeDatabase(post)
            }

        })
    }

    fun pickUserPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .setCamera(true)
            .startAlbumWithActivityResultCallback(photoLauncher)


    }

    val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                binding.ivPhoto.setImageURI(pickedPhoto)
            }
        }
}