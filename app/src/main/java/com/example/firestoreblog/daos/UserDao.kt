package com.example.firestoreblog.daos

import com.example.firestoreblog.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {

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