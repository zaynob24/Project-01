package com.example.socialmedia.repositories

import android.net.Uri
import android.util.Log
import com.example.socialmedia.model.Posts
import com.example.socialmedia.model.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

private const val POSTS = "posts"

private const val TAG = "FirebaseServiceReposito"
class FirebaseServiceRepository {

    val  firebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val imageRef = Firebase.storage.reference


    // Collections
    private val postInfoCollection = db.collection(POSTS)


    //-------------------------------------------------------------------------------------------------------//


    // Sign up
    fun signUp(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)


    // upload post Image to fireStorage
    fun uploadPostImage(imageUri: Uri, filename:String)= imageRef.child("images/$filename").putFile(imageUri)


    // upload post Info to fireStore
    fun uploadPostInfo(posts: Posts)= postInfoCollection.document().set(posts)

    // Login
    fun login(email: String, password: String)= firebaseAuth.signInWithEmailAndPassword(email,password)


    // retrieve posts
    suspend fun  retrievePosts() =  postInfoCollection.get().await()


    //-------------------------------------------------------------------------------------------------------//

    // This companion object is to makes our Firebase Service a singleton
    companion object {
        private var instance: FirebaseServiceRepository? = null

        fun init() {
            if (instance == null) {
                instance = FirebaseServiceRepository()
            }
        }

        fun get(): FirebaseServiceRepository {
            return instance ?: throw Exception("Firebase service repository must be initialized")
        }
    }


}