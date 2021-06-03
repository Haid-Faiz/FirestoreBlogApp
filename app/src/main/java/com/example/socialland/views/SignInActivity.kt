 package com.example.socialland.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.example.socialland.R
import com.example.socialland.repository.UserRepo
import com.example.socialland.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private val SIGN_IN_CODE: Int = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var btn: Button
    lateinit var progress: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        // Configure google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        googleSignInClient.signOut()

        progress = findViewById(R.id.progress_bar)
        btn = findViewById(R.id.google_signin_button)
        btn.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {

        // clearing previous signin caches
        googleSignInClient.signOut()

        //getting the google signin intent
        val signInIntent = googleSignInClient.signInIntent      // getSignInInIntent()
        startActivityForResult(signInIntent, SIGN_IN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask?.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken)
        } catch (e: ApiException) {
//            Toast.makeText(this, e.statusCode, Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        btn.visibility = View.GONE
        progress.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {

            auth.signInWithCredential(credential).await()
            val firebaseUser = auth.currentUser

            // we can update UI from UI thread & can't update UI from
            // background thread otherwise it will generate some errors
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {

            val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            val userDao = UserRepo()
            userDao.addUser(user)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            btn.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }
    }
}