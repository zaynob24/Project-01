package com.example.socialmedia.idintity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLoggedInState()

        binding.emailAdrresProfile.setText(firebaseAuth.currentUser?.email)
        binding.textView5.setText(firebaseAuth.currentUser?.displayName)


        binding.loginTV.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        //LogOut
        binding.logoutProfile.setOnClickListener {

            firebaseAuth.signOut()

            // clear User Info  in shared Pref
            checkLoggedInState()
        }

    }

    private fun checkLoggedInState() {

        firebaseAuth.currentUser?.let {

            // user logged in!
            binding.userInfoLayout.visibility = View.VISIBLE
            binding.userNotLoginLayout.visibility = View.INVISIBLE

        }?:run {
            // user are not logged in
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)


        }

    }

}