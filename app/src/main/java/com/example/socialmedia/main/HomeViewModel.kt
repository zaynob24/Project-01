package com.example.socialmedia.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Posts
import com.example.socialmedia.repositories.FirebaseServiceRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private val firebaseRepo = FirebaseServiceRepository.get()


    val retrievePostsLiveData = MutableLiveData<ArrayList<Posts>>()
    val retrievePostsErrorLiveData = MutableLiveData<String>()
    val selectedPostsLiveData = MutableLiveData<Posts>()


    fun retrievePosts() {

        Log.d(TAG, "retrievePosts")

        val postArrayList: ArrayList<Posts> = arrayListOf()

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = firebaseRepo.retrievePosts()

                for (document in response.documents) {
                    Log.d(TAG, "document" + document.toString())

                        //  val post = document.toString()
                    //val post = document.toObject<Posts>()
                    //val post = Gson().fromJson(document.data.toString(),Posts::class.java)

                    val postMassage = document.get("postMassage").toString()
                    val imageUrl = document.get("imageUrl").toString()
                    val imageName = document.get("imageName").toString()
                    val userId = document.get("userId").toString()
                    val userName = document.get("userName").toString()

                    postArrayList.add(Posts(postMassage,imageUrl,imageName,userId,userName))
                }

                //Log.d(TAG, itemArrayList.toString())
                retrievePostsLiveData.postValue(postArrayList)

                Log.d(TAG, "retrieveItems success: $response")


            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                retrievePostsErrorLiveData.postValue(e.message)
            }

        }
    }

}