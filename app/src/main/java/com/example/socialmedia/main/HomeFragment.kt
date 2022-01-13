package com.example.socialmedia.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_addPostsFragment)
        }

    }
}