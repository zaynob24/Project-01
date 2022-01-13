package com.example.socialmedia.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.databinding.CommentItemBinding
import com.example.socialmedia.model.Comment

private const val TAG = "CommentsAdapter"

class CommentsAdapter (private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {

        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context) , parent, false)

        Log.d(TAG, "onCreateViewHolder: $comments")

        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = comments[position]

        Log.d(TAG, "onBindViewHolder: comment: $comment")
        holder.bind(comment)
    }

    override fun getItemCount(): Int = comments.size





    inner class CommentsViewHolder(val binding:CommentItemBinding): RecyclerView.ViewHolder(binding.commentItemLayout){

        fun bind (comment:Comment){
            binding.commentText.text = comment.commentText
            binding.commentUserName.text = comment.userName
        }
    }
}