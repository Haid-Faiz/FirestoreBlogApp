package com.example.firestoreblog.model

class Post(
    val textInput: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val postImageUrl: String = "",
    val likedBy: ArrayList<String> = ArrayList()
)