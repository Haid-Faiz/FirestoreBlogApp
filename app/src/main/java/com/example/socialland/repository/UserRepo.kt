package com.example.socialland.repository

import com.example.socialland.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserRepo {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun addUser(user: User?){

        GlobalScope.launch(Dispatchers.IO){
            user?.let {
                usersCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUser(userID: String): Task<DocumentSnapshot> {
        return usersCollection.document(userID).get()    // this will return task
    }
}