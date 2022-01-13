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

    private lateinit var  name: String
    private lateinit var  email: String
    private lateinit var  password: String
    private lateinit var  confirmPassword: String




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
            findNavController().navigate(R.id.action_singeUpFragment_to_loginFragment)
        }

        observer()


        //----------------------------------------------------------------------------------------------------//

        // Register
        Log.d(TAG, "Before signUp Button clicked")
        binding.signUpButton.setOnClickListener {

            takeEntryData() // to collect items data from all fields
            if (checkEntryData()){ // to check if all field contain data and give error massage if not
                    progressDialog.show()

                    signUpViewModel.signUp(Users(name, email), password)
            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
            }

        }



    }


    fun observer() {
        signUpViewModel.signUpLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(),R.string.user_registers_successfully, Toast.LENGTH_SHORT).show()

               findNavController().navigate(R.id.action_singeUpFragment_to_homeFragment)
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


    //--------------------------------------------------------------------------------------------------------------//

    // to collect post data from all fields
    private fun takeEntryData() {

        name = binding.fullNameSignUpTV.text.toString().trim()
        email = binding.emailSignUpTV.text.toString().trim()
        password = binding.passwordSignUpTV.text.toString().trim()
        confirmPassword = binding.confirmPasswordSignUpTV.text.toString().trim()
    }

    //--------------------------------------------------------------------------------------------------------------//

    // to check if all field contain data and give error massage if not
    private fun checkEntryData() : Boolean {
        var isAllDataFilled = true

        //check name
        if (name.isEmpty() || name.isBlank()) {
            binding.fullNameSignUpTV.error = getString(R.string.required)
            isAllDataFilled = false
        } else {
            binding.fullNameSignUpTV.error = null
        }


        //check email
        if (email.isEmpty() || email.isBlank()) {
            binding.emailSignUpTV.error = getString(R.string.required)
            isAllDataFilled = false
        } else {
            binding.emailSignUpTV.error = null
        }

        //check password
        if (password.isEmpty() || password.isBlank()) {
            binding.passwordSignUpTV.error = getString(R.string.required)
            isAllDataFilled = false
        } else {
            binding.passwordSignUpTV.error = null
        }

        //check confirmPassword
        if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
            binding.confirmPasswordSignUpTV.error = getString(R.string.required)
            isAllDataFilled = false
        }else if(password != confirmPassword){
            binding.confirmPasswordSignUpTV.error = getString(R.string.password_not_match)
            isAllDataFilled = false
        }else {
            binding.confirmPasswordSignUpTV.error = null
        }

        return isAllDataFilled
    }
}