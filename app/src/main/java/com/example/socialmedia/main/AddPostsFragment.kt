package com.example.socialmedia.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.MainActivity
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentAddPostsBinding
import com.example.socialmedia.dialogs.ImageDialogFragment
import com.example.socialmedia.model.Posts
import com.example.socialmedia.util.Permissions
import com.google.firebase.auth.FirebaseAuth
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy


private const val TAG = "AddPostsFragment"
class AddPostsFragment : Fragment() {

    private var imageUri: Uri? = null
    private var imageFileName = ""
    val  firebaseAuth = FirebaseAuth.getInstance()

    private val IMAGE_PICKER = 0
    private lateinit var binding: FragmentAddPostsBinding

    private val addPostsViewModel: AddPostsViewModel by activityViewModels()

    private lateinit var progressDialog: ProgressDialog

    private lateinit var  postMasage: String


    //-----------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Loading...")
        progressDialog.setCancelable(false)

        binding = FragmentAddPostsBinding.inflate(inflater, container, false)
        return binding.root

    }

    //-----------------------------------------------------------------------------------//


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //checkLoggedInState()

        // if user not login
//        binding.loginTV.setOnClickListener {
//            findNavController().navigate(R.id.action_addItemFragment_to_loginFragment)
//        }



        //-----------------------------------------------------------------//

        //  show image picker for user to chose post image
        binding.attachButton.setOnClickListener {

            Permissions.checkPermission(requireContext(), requireActivity())
            showImagePicker()     // this function showing ImagePicker using Matisse library then give imageUri of chosen image
        }

        // cancel adding post
//        binding.cancelAddPostIcon.setOnClickListener {
//            activity?.supportFragmentManager?.popBackStack()
//        }

        //cancel chosen image
        binding.cancelImageIcon.setOnClickListener {

            binding.imagePostView.visibility = View.INVISIBLE
            binding.cancelImageIcon.visibility = View.INVISIBLE
            imageUri = null

        }


        //-----------------------------------------------------------------//

        // save post
        binding.publishButton.setOnClickListener {

            takeEntryData() // to collect post data from all fields

            if (checkEntryData()){ // to check if all field contain data and give error massage if not

                imageUri?.let {
                    progressDialog.show()
                    // pass time in millis to use it as filename of the image
                    // to be sure it is unique in fireStorage(Duplicated name will replace the old image instead of add new one!)
                    addPostsViewModel.uploadPostImage(it, System.currentTimeMillis().toString())
                }?: addPostsViewModel.uploadPostInfo(Posts(postMasage,"",imageFileName,firebaseAuth.currentUser!!.uid,firebaseAuth.currentUser!!.displayName!!))  // save post details (postMassage,image )to fireStore


            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
            }


        }
        //-----------------------------------------------------------------//


        //open dialog tha show full screen image
        binding.imagePostView.setOnClickListener {

            val activity = requireContext() as? MainActivity
            imageUri?.let { uri ->
                ImageDialogFragment(uri).show(
                    activity!!.supportFragmentManager,
                    "DetailsDialogFragment"
                )
            }
        }

        //-----------------------------------------------------------------//

        observer()

    }

    //--------------------------------------------------------------------------------------------------------------//

    // to collect post data from all fields
    private fun takeEntryData() {

        postMasage = binding.postMasageEditText.text.toString().trim()

    }

    // to check if all field contain data and give error massage if not
    private fun checkEntryData() : Boolean {
        var isAllDataFilled  = true

        //check description
        if (postMasage.isEmpty()|| postMasage.isBlank()){
            binding.postMasageEditText.error = getString(R.string.required)
            isAllDataFilled = false
        }else{
            binding.postMasageEditText.error = null
        }

        return isAllDataFilled
    }

    //--------------------------------------------------------------------------------------------------------------//

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {

            //using Matisse library to take uri of chosen image
            imageUri = Matisse.obtainResult(data)[0] //[0] index 0 to take first index of the array of photo selected

            binding.imagePostView.visibility = View.VISIBLE
            binding.cancelImageIcon.visibility = View.VISIBLE

            binding.imagePostView.setImageURI(imageUri)

        }

    }

    //--------------------------------------------------------------------------------------------------------------//

    // showing ImagePicker using Matisse library
    fun showImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofImage(), false) // image or image and video or whatever
            .captureStrategy(CaptureStrategy(true, "com.example.moqaida"))
            .forResult(IMAGE_PICKER)
    }

    //--------------------------------------------------------------------------------------------------------------//

    @SuppressLint("SetTextI18n")
    private fun observer() {
        // after uploading post image to fireStorage finish
        // post upload successfully
        addPostsViewModel.uploadImageLiveData.observe(viewLifecycleOwner, {
            it?.let {

                imageFileName = it //name of image in fireStorage (the name is: currentTimeMillis)

                val imageUrl = "https://firebasestorage.googleapis.com/v0/b/socialmedia-b66ed.appspot.com/o/images%2F$imageFileName?alt=media&token=167a9a01-1c56-4e64-a74d-b937eac2a495"

                addPostsViewModel.uploadImageLiveData.postValue(null)

                 // save post details (postMassage,image )to fireStore
                addPostsViewModel.uploadPostInfo(Posts(postMasage,imageUrl,imageFileName,firebaseAuth.currentUser!!.uid,firebaseAuth.currentUser!!.displayName!!))

            }
        })

        addPostsViewModel.uploadImageErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                addPostsViewModel.uploadImageErrorLiveData.postValue(null)
            }
        })
        //-----------------------------------------------------------------------------------//

        // after uploading posts to fireStore finish
        //  upload successfully
        addPostsViewModel.uploadPostsLiveData.observe(viewLifecycleOwner,{
            it?.let {

                progressDialog.dismiss()  // To close the progress Dialog after uploading image
                Toast.makeText(requireActivity(), R.string.post_published_successfully, Toast.LENGTH_SHORT).show()
                addPostsViewModel.uploadPostsLiveData.postValue(null)
                findNavController().popBackStack()
            }
        })


        addPostsViewModel.uploadItemPostsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                addPostsViewModel.uploadItemPostsLiveData.postValue(null)
            }
        })

    }


    //--------------------------------------------------------------------------------------------------------------//

//    private fun checkLoggedInState() {
//
//        firebaseAuth.currentUser?.let {
//
//            // user logged in!
//            binding.addItemLayout.visibility = View.VISIBLE
//            binding.addItemNotLoginLayout.visibility = View.GONE
//
//        }?:run {
//            // user are not logged in
//            binding.addItemNotLoginLayout.visibility = View.VISIBLE
//            binding.addItemLayout.visibility = View.GONE
//
//        }
//
//    }
}