package com.example.socialmedia.dialogs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentImageDialogBinding
import com.squareup.picasso.Picasso

private const val TAG = "ImageDialogHomeFragment"
class ImageDialogHomeFragment(val imageUri: String) : DialogFragment() {

    private lateinit var binding: FragmentImageDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

           Log.d(TAG,imageUri)


        Picasso.get().load(imageUri).into(binding.fullScrrenImageDialog);


        binding.closeImageIcon.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
         val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
         val high = (resources.displayMetrics.widthPixels * 0.85).toInt()

        dialog!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}