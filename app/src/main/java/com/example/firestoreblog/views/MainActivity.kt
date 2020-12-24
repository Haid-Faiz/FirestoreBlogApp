package com.example.firestoreblog.views

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.firestoreblog.R
import com.example.firestoreblog.adapter.PostAdapter
import com.example.firestoreblog.daos.PostDao
import com.example.firestoreblog.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity(), PostAdapter.ClickListenerInterface {

    lateinit var postAdapter: PostAdapter
    var postDao: PostDao = PostDao()
    val postCollection = postDao.postCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floating_button.setOnClickListener{
            startActivity(Intent(this, UploadPost::class.java))
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        progress_bar_main.visibility = View.VISIBLE

        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)   // here after query, we are passing it's model
            .build()

        postAdapter = PostAdapter(firestoreRecyclerOptions, this)
        postAdapter.isVisible = false
        recycler_view.adapter = postAdapter
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        progress_bar_main.visibility = View.GONE
//        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        postAdapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure ?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                Firebase.auth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            .create().show()

        return super.onOptionsItemSelected(item)
    }

    override fun onLikeClicked(postID: String) {
        postDao.updateLikes(postID)
    }

    override fun onMenuClicked(postID: String) {
        postDao.deletePost(postID, recycler_view)
    }

    override fun onShareClicked(postID: String) {

        GlobalScope.launch {
            val currentPost: Post? =
                postCollection.document(postID).get().await().toObject(Post::class.java)
            val shareIntent = Intent(Intent.ACTION_SEND).setType("text/plain")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "I found this post interesting...\n\n" + currentPost!!.textInput + "\n" + currentPost.postImageUrl
            )
            val chooser = Intent.createChooser(shareIntent, "Share it with")
            startActivity(chooser)
        }
    }
}