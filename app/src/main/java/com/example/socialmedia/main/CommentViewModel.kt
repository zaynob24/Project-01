package com.example.socialmedia.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialmedia.model.Comment
import com.example.socialmedia.model.Posts
import com.example.socialmedia.repositories.FirebaseServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

private const val TAG = "CommentViewModel"

class CommentViewModel : ViewModel() {

    private val firebaseRepo = FirebaseServiceRepository.get()


    fun retriveCommentsFlow (postId: String): Flow<List<Comment>> {
        val commentsFlow = flow<List<Comment>>{
            try {
                val response = firebaseRepo.retrieveComments(postId)

                val listofComments : MutableList<Comment> = mutableListOf()

                for (document in response.documents){
                    val comment = Comment()

                    comment.commentText = document.get("commentText").toString()
                    comment.userName = document.get("userName").toString()
                    comment.userPic = document.get("userPic").toString()

                    listofComments.add(comment)
                }

                emit(listofComments)
            }catch (e: Exception){
                Log.d(TAG, "retriveCommentsFlow: ${e.message}")
            }
        }

        return commentsFlow
    }
}