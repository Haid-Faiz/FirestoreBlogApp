package com.example.firestoreblog.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firestoreblog.R
import com.example.firestoreblog.Utils
import com.example.firestoreblog.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(
    options: FirestoreRecyclerOptions<Post>,
    val clickListenerListener: ClickListenerInterface
) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    var isVisible: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val viewHolder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
        viewHolder.likeButton.setOnClickListener {
            clickListenerListener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.menuButton.setOnClickListener {


            // this popup menu needs to be tied to a view
            val popupMenu: PopupMenu = PopupMenu(it.context, it)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {

                    return when (item!!.itemId) {
                        R.id.delete_menu -> {
                            clickListenerListener.onMenuClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
                            true
                        }
                        else -> false
                    }
                }
            })
            popupMenu.show()
        }
        viewHolder.shareButton.setOnClickListener {
            clickListenerListener.onShareClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {

        holder.userName.text = model.createdBy.displayName
        holder.postTitle.text = model.textInput
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop()
            .into(holder.userImage)
        Glide.with(holder.postImage.context).load(model.postImageUrl).centerCrop()
            .into(holder.postImage)
        holder.likeCount.text = model.likedBy.size.toString()

        val formattedTimeStamp = Utils().getTimeAgo(model.createdAt)
        holder.timeStamp.text = formattedTimeStamp

        val auth = Firebase.auth
        val currentUserID = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserID)
        if (isLiked) {
            holder.likeButton.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.likeButton.context,
                    R.drawable.ic_baseline_liked_24
                )
            )
        } else {
            holder.likeButton.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.likeButton.context,
                    R.drawable.ic_baseline_unliked_24
                )
            )
        }

        if (isVisible)
            holder.menuButton.visibility = View.VISIBLE
        else
            holder.menuButton.visibility = View.GONE
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val userImage = itemView.findViewById<ImageView>(R.id.user_image)
        val timeStamp = itemView.findViewById<TextView>(R.id.timestamp)
        val postTitle = itemView.findViewById<TextView>(R.id.post_title)
        val postImage = itemView.findViewById<ImageView>(R.id.post_image)
        val likeCount = itemView.findViewById<TextView>(R.id.like_count)
        val likeButton = itemView.findViewById<ImageButton>(R.id.like_button)
        val menuButton = itemView.findViewById<ImageButton>(R.id.popup_menu_button)
        val shareButton = itemView.findViewById<ImageButton>(R.id.share_button)
    }

    interface ClickListenerInterface {
        fun onLikeClicked(postID: String)
        fun onMenuClicked(postID: String)
        fun onShareClicked(postID: String)
    }
}