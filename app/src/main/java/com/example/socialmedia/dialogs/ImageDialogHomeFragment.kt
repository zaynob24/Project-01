package com.example.socialmedia.dialogs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentImageDialogBinding


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


        getContext()?.let {
            Glide
                .with(it)
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true) // to stop Cache so when add new post the list updated
                .placeholder(R.drawable.square_shape)
                .into(binding.fullScrrenImageDialog)
        }



        binding.closeImageIcon.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}