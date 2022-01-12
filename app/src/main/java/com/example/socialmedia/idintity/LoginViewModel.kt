package com.example.socialmedia.idintity

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Users
import com.example.socialmedia.repositories.FirebaseServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"
// AndroidViewModel -> to use application in getSharedPreferences
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepo = FirebaseServiceRepository.get()

    val loginLiveData = MutableLiveData<String>()
    val loginErrorLiveData = MutableLiveData<String>()


    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.login(email, password)

                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // post success to live data
                        loginLiveData.postValue("success login")
                        Log.d(TAG, "Login success: $response")

                    } else {
                        Log.d(TAG, task.exception!!.message.toString())
                        loginErrorLiveData.postValue(task.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: login ${e.message}")
                loginErrorLiveData.postValue(e.message)
            }
        }
    }

}