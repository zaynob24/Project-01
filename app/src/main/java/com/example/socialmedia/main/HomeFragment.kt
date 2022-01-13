package com.example.socialmedia.main

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.adapters.PostsAdapter
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {


    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: FragmentHomeBinding

    private lateinit var postsRecyclerViewAdapter : PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progressDialog = ProgressDialog(requireActivity()).also {
            it.setTitle("Loading...")
            it.setCancelable(false)
        }


        homeViewModel.retrievePosts()

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_addPostsFragment)
        }

        postsRecyclerViewAdapter = PostsAdapter(requireContext(),homeViewModel)
        binding.postsRecyclerView.adapter = postsRecyclerViewAdapter
        observer()

    }

    //--------------------------------------------------------------------------------------------------------//
    private fun observer() {
        homeViewModel.retrievePostsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG,it.toString())
                progressDialog.dismiss()
                postsRecyclerViewAdapter.submitList(it)
                homeViewModel.retrievePostsLiveData.postValue(null)

            }
        })

        homeViewModel.retrievePostsErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                homeViewModel.retrievePostsErrorLiveData.postValue(null)
            }
        })    }
}