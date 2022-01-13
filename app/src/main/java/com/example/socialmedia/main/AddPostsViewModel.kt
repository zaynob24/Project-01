package com.example.socialmedia.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Posts
import com.example.socialmedia.repositories.FirebaseServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "AddItemViewModel"

class AddPostsViewModel : ViewModel() {
    private val firebaseRepo = FirebaseServiceRepository.get()

    val uploadImageLiveData = MutableLiveData<String>()
    val uploadImageErrorLiveData = MutableLiveData<String>()

    val uploadPostsLiveData = MutableLiveData<String>()
    val uploadItemPostsLiveData = MutableLiveData<String>()

    fun uploadPostInfo(posts: Posts) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = firebaseRepo.uploadPostInfo(posts)
                response.addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        // we need something to observe so we use any word EX:("success")
                        uploadPostsLiveData.postValue("success")

                        Log.d(TAG, "Post upload success: $response")

                    } else {
                        Log.d(TAG, task.exception!!.message.toString())
                        uploadItemPostsLiveData.postValue(task.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                uploadItemPostsLiveData.postValue(e.message)
            }
        }

    }

    //-------------------------------------------------------------------------------------------------------------//
    // to upload post Image to fireStorage
    fun uploadPostImage(imageUri: Uri, filename: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = firebaseRepo.uploadPostImage(imageUri, filename)
                response.addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        // post filename to use it in upload post info
                        uploadImageLiveData.postValue(filename)
                        Log.d(TAG, "Image upload success: $response")

                    } else {
                        Log.d(TAG, task.exception!!.message.toString())
                        uploadImageErrorLiveData.postValue(task.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                uploadImageErrorLiveData.postValue(e.message)
            }
        }
    }


    //-------------------------------------------------------------------------------------------------------------//

}