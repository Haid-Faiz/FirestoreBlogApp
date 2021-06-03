package com.example.socialland.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.socialland.R
import com.example.socialland.repository.PostRepo

class UploadPost : AppCompatActivity() {


    private val GALLERY_REQUEST_CODE: Int = 12
    lateinit var imageUri: Uri
    val postRepo: PostRepo = PostRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

//        upload_button.setOnClickListener {
//            val textInput = post_input.editText?.text.toString().trim()
//
//            if (textInput.isNotEmpty()){
////                postRepo.uploadImage(imageUri)
//                postRepo.addPost(textInput, imageUri)
//                finish()
//            }
//        }

//        add_image.setOnClickListener {
//            // var intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE){
            imageUri = data?.data!!
//            Glide.with(this).load(imageUri).centerCrop().into(add_image)
        }
    }
}