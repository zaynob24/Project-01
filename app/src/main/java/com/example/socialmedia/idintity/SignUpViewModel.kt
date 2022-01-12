package com.example.socialmedia.idintity

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Users
import com.example.socialmedia.repositories.*
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "SignUpViewModel"
// AndroidViewModel -> to use application in getSharedPreferences
class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepo = FirebaseServiceRepository.get()

    val signUpLiveData = MutableLiveData<String>()
    val signUpErrorLiveData = MutableLiveData<String>()


    fun signUp(user: Users, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.signUp(user.email, password)

                response.addOnCompleteListener {
                    if (it.isSuccessful) {
                        insertUserInfo(user)
                    }else{
                        signUpErrorLiveData.postValue(response.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                signUpErrorLiveData.postValue(e.message)
            }
        }
    }
    private fun insertUserInfo(user: Users) {


        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(user.fullName)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firebaseRepo.firebaseAuth.currentUser?.updateProfile(profileUpdates)?.await()

                signUpLiveData.postValue("Success")

            } catch(e: Exception) {
                signUpErrorLiveData.postValue(e.message)

            }
        }

    }


}
