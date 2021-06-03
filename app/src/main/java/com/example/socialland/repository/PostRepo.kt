package com.example.socialland.repository

import android.net.Uri
import android.view.View
import com.example.socialland.model.Post
import com.example.socialland.model.User
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostRepo {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth
    val currentUserID = auth.currentUser!!.uid

    // Firebase Storage
    val firebaseStorage = FirebaseStorage.getInstance()
    val storageReference = firebaseStorage.reference

    fun addPost(textInput: String, imageUri: Uri) {

        GlobalScope.launch(Dispatchers.IO) {
            val userDao = UserRepo()
            val task = userDao.getUser(currentUserID)
            // Now, as it is a task so we need to add complete listener to it... but
            // here we will use await() method of CoRoutine
            val user = task.await().toObject(User::class.java)!!
            // we are getting document snapshot but not getting actual document
            // here we have casted it to User class


            /*   filePath.putFile(postImageUri).addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   task.result!!.storage.downloadUrl.addOnSuccessListener { uri ->
                       val downloadUrl = uri.toString()
                   }
               }
           }
             //correct one
             val result = it.metadata!!.reference!!.downloadUrl
             result.addOnSuccessListener {
                 val url = it.toString()
             }
             // correct two
             .addOnSuccessListener {
             it.storage.downloadUrl.addOnCompleteListener {
                 downloadURL = it.result.toString()
             }
         }
  */
            val filePath =
                storageReference.child("MyBlogImages").child(imageUri.lastPathSegment.toString())
            val taskOne: UploadTask.TaskSnapshot = filePath.putFile(imageUri).await()

            val taskTwo: Uri = taskOne.storage.downloadUrl.await()
            val downloadURL = taskTwo.toString()

            val currentTime: Long = System.currentTimeMillis()

            val post = Post(
                textInput = textInput,
                createdBy = user,
                createdAt = currentTime,
                postImageUrl = downloadURL
            )
            postCollection.document().set(post)
        }
    }

    fun updateLikes(postID: String) {
        val currentUserID = auth.currentUser!!.uid
        val task: Task<DocumentSnapshot> = getPostByID(postID)
        GlobalScope.launch {
            val post = task.await().toObject(Post::class.java)

            val isLiked = post!!.likedBy.contains(currentUserID)
            if (isLiked) {
                post.likedBy.remove(currentUserID)
            } else {
                post.likedBy.add(currentUserID)
            }
            postCollection.document(postID).set(post)
        }
    }

    private fun getPostByID(postID: String): Task<DocumentSnapshot> =
        postCollection.document(postID).get()

    fun deletePost(postID: String, view: View) {

        GlobalScope.launch(Dispatchers.IO) {

            val currentPost: Post =
                postCollection.document(postID).get().await().toObject(Post::class.java)!!

            val isMatched = currentPost.createdBy.uid.equals(currentUserID)

            if (isMatched) {
                postCollection.document(postID).delete()
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                        postCollection.document(postID).set(currentPost)
                    }.show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "You can only delete your posts", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

}

// we can await tasks