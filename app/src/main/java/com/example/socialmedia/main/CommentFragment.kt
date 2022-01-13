package com.example.socialmedia.main


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.socialmedia.adapters.CommentsAdapter
import com.example.socialmedia.adapters.POST_ID_KEY
import com.example.socialmedia.databinding.FragmentCommentBinding
import com.example.socialmedia.model.Comment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "CommentFragment"

class CommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding

    private val commentViewModel : CommentViewModel by activityViewModels()

    private lateinit var postId:String

//    private val args : Comment

//    private val args: Comme by navArgs<>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = arguments?.getString(POST_ID_KEY)!!

        Log.d(TAG, "onCreate: post ID : $postId")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater,container,false )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentViewModel.retrieveCommentsFlow(postId).onEach {
            updateUI(it)
        }.launchIn(lifecycleScope)

//        lifecycleScope.launch {
//            commentViewModel.retrieveCommentsFlow(postId).collectLatest { comments ->
//                Log.d(TAG, "onCreate: ID: $postId")
//                Log.d(TAG, "onCreate: comments : $comments")
//                updateUI(comments)
//            }
//        }
    }

    override fun onStart() {
        super.onStart()

        binding.imageButton.setOnClickListener {
            commentViewModel.uploadNewComment(postId,
                Comment(commentText = binding.editTextTextPersonName3.text.toString() , userName = commentViewModel.auth.currentUser!!.displayName.toString())
            )
            binding.editTextTextPersonName3.setText("")
            commentViewModel.retrieveCommentsFlow(postId).onEach {
                updateUI(it)
            }.launchIn(lifecycleScope)

        }
    }

    private fun updateUI(comments: List<Comment>){

        Log.d(TAG, "updateUI: fragment comments : $comments")
        val commentsAdapter = CommentsAdapter(comments)

        binding.commentsRv.adapter = commentsAdapter
    }

}