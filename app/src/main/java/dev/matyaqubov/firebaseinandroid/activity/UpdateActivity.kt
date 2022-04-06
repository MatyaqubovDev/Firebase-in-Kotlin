package dev.matyaqubov.firebaseinandroid.activity

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

import dev.matyaqubov.firebaseinandroid.databinding.ActivityUpdateBinding
import dev.matyaqubov.firebaseinandroid.manager.DatabaseHandler
import dev.matyaqubov.firebaseinandroid.manager.DatabaseManager
import dev.matyaqubov.firebaseinandroid.manager.StorageHandler
import dev.matyaqubov.firebaseinandroid.manager.StorageManager
import dev.matyaqubov.firebaseinandroid.model.Post
import java.lang.Exception

class UpdateActivity : BaseActivity() {
    lateinit var binding: ActivityUpdateBinding
    var pickedPhoto: Uri? = null
    var post:Post= Post()
    var allPhotos = ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        binding.apply {
            bCreate.setOnClickListener {
                var title = etTitle.text.toString()
                var body = etBody.text.toString()
                post.body=body
                post.title=title
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

    private fun setData() {
        post= intent.getSerializableExtra("post") as Post
        binding.apply {
            if (post.img!=null) ivPhoto.setImageURI(post.img.toUri())
            etTitle.setText(post.title)
            etBody.setText(post.body)

        }
    }

    private fun storeDatabase(post: Post) {
        showLoading(this)
        DatabaseManager.apiUpdatePost(post, object : DatabaseHandler {
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