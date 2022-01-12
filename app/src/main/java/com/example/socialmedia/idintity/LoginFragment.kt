package com.example.socialmedia.idintity

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
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
import com.example.socialmedia.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireActivity()).also {
            it.setTitle("Loading...")
            it.setCancelable(false)
        }


        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_singeUpFragment2)
        }

        observer()


        binding.loginButton.setOnClickListener {
            val email: String = binding.emailLoginTV.text.toString().trim()
            val password: String = binding.passwordLoginTV.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                progressDialog.show()
                Log.d(TAG, "Inside if password == confirmPassword")
                loginViewModel.login(email, password)

            }
        }
    }

    private fun observer() {

        // login observer
        loginViewModel.loginLiveData.observe(viewLifecycleOwner, { email ->
            email?.let {

                progressDialog.dismiss()
                Toast.makeText(requireActivity(), R.string.user_logged_in_successfully, Toast.LENGTH_SHORT).show()


                loginViewModel.loginLiveData.postValue(null)
                //checkLoggedInState()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            }
        })

        loginViewModel.loginErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                loginViewModel.loginErrorLiveData.postValue(null)
            }
        })

    }

}