package com.example.socialmedia.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialmedia.model.Comment
import com.example.socialmedia.repositories.FirebaseServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

private const val TAG = "CommentViewModel"

class CommentViewModel : ViewModel() {

    private val firebaseRepo = FirebaseServiceRepository.get()

    val auth = firebaseRepo.firebaseAuth


    fun retrieveCommentsFlow (postId: String): Flow<List<Comment>> {
        val commentsFlow = flow<List<Comment>>{
            try {
                val response = firebaseRepo.retrieveComments(postId)

                Log.d(TAG, "retrieveCommentsFlow: ID : $postId")

                Log.d(TAG, "retrieveCommentsFlow: response $response")

                val listOfComments : MutableList<Comment> = mutableListOf()

                for (document in response.documents){
                    val comment = Comment()

                    Log.d(TAG, "retrieveCommentsFlow: document: $document")
                    comment.commentText = document.get("commentText").toString()
                    comment.userName = document.get("userName").toString()
                    comment.userPic = document.get("userPic").toString()

                    Log.d(TAG, "retrieveCommentsFlow: comment text: ${document.get("commentText").toString()}")
                    Log.d(TAG, "retriveCommentsFlow: comment: $comment")
                    listOfComments.add(comment)
                }

                Log.d(TAG, "retriveCommentsFlow: comments list $listOfComments")
                emit(listOfComments)
            }catch (e: Exception){
                Log.d(TAG, "retriveCommentsFlow: ${e.message}")
            }
        }

        return commentsFlow
    }

    fun uploadNewComment(postId: String,comment: Comment) = firebaseRepo.uploadNewComment(postId,comment)
}