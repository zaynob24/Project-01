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

private const val USER= "users"
const val SHARED_PREF_FILE = "Auth"
const val USER_ID = "userId"
const val USER_EMAIL = "userEmail"
const val USER_PHONE = "userPhone"
const val USER_NAME = "userFullName"
const val IMAGE_NAME = "imageName"

const val EMAIL = "email"

private const val ITEM = "items"
private const val REQUESTS = "requests"

private const val TAG = "FirebaseServiceReposito"
class FirebaseServiceRepository {

    val  firebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val imageRef = Firebase.storage.reference


    // Collections
    private val userCollection = db.collection(USER)
    private val itemInfoCollection = db.collection(ITEM)


    //-------------------------------------------------------------------------------------------------------//


    // Sign up
    fun signUp(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)


    // upload Item Image to fireStorage
    fun uploadItemImage(imageUri: Uri, filename:String)= imageRef.child("images/$filename").putFile(imageUri)


    // upload Item Info to fireStore
    fun uploadItemInfo(posts: Posts)= itemInfoCollection.document().set(posts)

    // Login
    fun login(email: String, password: String)= firebaseAuth.signInWithEmailAndPassword(email,password)


    // retrieve Items
    //suspend fun  retrieveItems() =  itemInfoCollection.get().await()


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