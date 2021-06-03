package com.example.socialland.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialland.R
import com.example.socialland.adapter.PostAdapter

class MainActivity : AppCompatActivity(), PostAdapter.ClickListenerInterface {
//
//    lateinit var postAdapter: PostAdapter
//    var postRepo: PostRepo = PostRepo()
//    val postCollection = postRepo.postCollection
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        floating_button.setOnClickListener{
//            startActivity(Intent(this, UploadPost::class.java))
//        }
//
//        setUpRecyclerView()
    }
//
//    private fun setUpRecyclerView() {
//        progress_bar_main.visibility = View.VISIBLE
//
//        val query: Query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
//        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> = FirestoreRecyclerOptions.Builder<Post>()
//            .setQuery(query, Post::class.java)   // here after query, we are passing it's model
//            .build()
//
//        postAdapter = PostAdapter(firestoreRecyclerOptions, this)
//        postAdapter.isVisible = false
//        recycler_view.adapter = postAdapter
//        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//
//        progress_bar_main.visibility = View.GONE
////        recycler_view.layoutManager = LinearLayoutManager(this)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        postAdapter.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        postAdapter.stopListening()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        val alertDialog = AlertDialog.Builder(this)
//            .setTitle("Logout")
//            .setMessage("Are you sure ?")
//            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
//                Firebase.auth.signOut()
//                startActivity(Intent(this, SignInActivity::class.java))
//                finish()
//            })
//            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
//                dialogInterface.dismiss()
//            })
//            .create().show()
//
//        return super.onOptionsItemSelected(item)
//    }
//
    override fun onLikeClicked(postID: String) {
//        postRepo.updateLikes(postID)
    }
//
    override fun onMenuClicked(postID: String) {
//        postRepo.deletePost(postID, recycler_view)
    }

    override fun onShareClicked(postID: String) {

//        GlobalScope.launch(Dispatchers.IO) {
//            val currentPost: Post? =
//                postCollection.document(postID).get().await().toObject(Post::class.java)
//            val shareIntent = Intent(Intent.ACTION_SEND).setType("text/plain")
//            shareIntent.putExtra(
//                Intent.EXTRA_TEXT,
//                "I found this post interesting...\n\n" + currentPost!!.textInput + "\n" + currentPost.postImageUrl
//            )
//            val chooser = Intent.createChooser(shareIntent, "Share it with")
//            startActivity(chooser)
//        }
    }
}