package com.example.socialmedia.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.socialmedia.R
import com.example.socialmedia.adapters.POST_ID_KEY
import com.example.socialmedia.databinding.FragmentCommentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding

    private val commentViewModel : CommentViewModel by activityViewModels()

//    private val args : Comment

//    private val args: Comme by navArgs<>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val postId = arguments?.getString(POST_ID_KEY)

        lifecycleScope.launch {
            commentViewModel.retriveCommentsFlow(postId!!).collect { comments ->

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater,container,false )

        return binding.root
    }



}