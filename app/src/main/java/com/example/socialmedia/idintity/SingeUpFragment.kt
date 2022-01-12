package com.example.socialmedia.idintity

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
import com.example.socialmedia.databinding.FragmentSingeUpBinding
import com.example.socialmedia.model.Users
import com.google.firebase.auth.*
import io.grpc.InternalChannelz.id

private const val TAG = "SingeUpFragment"
class SingeUpFragment : Fragment() {

    private lateinit var binding: FragmentSingeUpBinding
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var progressDialog: ProgressDialog


    //create an instance of FirebaseAuth and initialize it with the FirebaseAuth.getInstance() method
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progressDialog = ProgressDialog(requireActivity()).also {
            it.setTitle("Loading...")
            it.setCancelable(false)
        }



        //-----------------------------------------------------------------------------------------------------------//


        binding = FragmentSingeUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate the user to Login page (LoginFragment)
        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_singeUpFragment2_to_loginFragment)
        }

        observer()


        //----------------------------------------------------------------------------------------------------//

        // Register
        Log.d(TAG, "Before signUp Button clicked")
        binding.signUpButton.setOnClickListener {
            val name = binding.fullNameSignUpTV.text.toString().trim()
            val email: String = binding.emailSignUpTV.text.toString().trim()
            val password: String = binding.passwordSignUpTV.text.toString().trim()
            val confirmPassword = binding.confirmPasswordSignUpTV.text.toString().trim()
            Log.d(TAG, "inside signUp Button clicked")

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    progressDialog.show()
                    Log.d(TAG, "Inside if password == confirmPassword")
                    signUpViewModel.signUp(Users(name, email), password)

                }else{
                    //TODO : give error massage
                    Toast.makeText(requireContext(),getText(R.string.password_not_match), Toast.LENGTH_SHORT).show()
                }
            }else{
                //TODO : check all fields not empty,give error massage
                Toast.makeText(requireContext(),"fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun observer() {
        signUpViewModel.signUpLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(),R.string.user_registers_successfully, Toast.LENGTH_SHORT).show()

               findNavController().navigate(R.id.action_singeUpFragment2_to_homeFragment)
                signUpViewModel.signUpLiveData.postValue(null)
            }
        })

        signUpViewModel.signUpErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                signUpViewModel.signUpErrorLiveData.postValue(null)
            }
        })
    }
//------------------------------------------------------------------------------------------------------------//

}