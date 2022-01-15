package com.example.socialmedia.adapters


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialmedia.MainActivity
import com.example.socialmedia.R
import com.example.socialmedia.databinding.TimelineLayoutBinding
import com.example.socialmedia.dialogs.ImageDialogFragment
import com.example.socialmedia.dialogs.ImageDialogHomeFragment
import com.example.socialmedia.main.HomeViewModel
import com.example.socialmedia.model.Posts

const val POST_ID_KEY = "postID"

private const val TAG = "PostsAdapter"
// context to use it with Glide , homeViewModel to use it for pass item selected
class PostsAdapter(val context: Context, val homeViewModel: HomeViewModel) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {


    // the adapter will change all adapter item if we give it new data(delete old one then add new one) but
    // if we use DiffUtil  it will keep old data and just change or add the new one

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Posts>() {
        override fun areItemsTheSame(oldPost: Posts, newPost: Posts): Boolean {
            Log.d(TAG,"arePostsTheSame")

            return oldPost.imageUrl == newPost.imageUrl
        }
        override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this,DIFF_CALLBACK)

    // to give the differ our data (the list)
    fun submitList(list:List<Posts>){
        Log.d(TAG,"submitList")

        differ.submitList(list)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostsAdapter.PostsViewHolder {

        val binding = TimelineLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

        val post = differ.currentList[position]

        holder.itemView.setOnClickListener{
            homeViewModel.selectedPostsLiveData.postValue(post)

            val args = Bundle()
            args.putString(POST_ID_KEY, post.postId)
            holder.itemView.findNavController().navigate(R.id.action_homeFragment_to_commentFragment, args)
        }

        holder.binding.commentsIcon.setOnClickListener{
            homeViewModel.selectedPostsLiveData.postValue(post)

            val args = Bundle()
            args.putString(POST_ID_KEY, post.postId)
            holder.itemView.findNavController().navigate(R.id.action_homeFragment_to_commentFragment, args)
        }

        holder.binding.postImageView.setOnClickListener {
            val activity = context as? MainActivity

                ImageDialogHomeFragment(post.imageUrl).show(
                    activity!!.supportFragmentManager,
                    "DetailsDialogFragment"
                )

        }

        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


  inner  class PostsViewHolder(val binding: TimelineLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post:Posts){

            Log.d(TAG,post.postMassage)
            binding.postMassageTextView.text = post.postMassage

            Log.d(TAG,post.imageUrl)

            binding.userImage.setImageResource(R.drawable.profile_icon)
            binding.userNameTextView.text = post.userName
            if (post.imageUrl.isEmpty()||post.imageUrl.isBlank()){

                binding.postImageView.visibility = View.GONE
            }else{
                binding.postImageView.visibility = View.VISIBLE
            }

            Glide
                .with(context)
                .load(post.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true) // to stop Cache so when add new post the list updated
                .placeholder(R.drawable.square_shape)
                .into(binding.postImageView)

        }

    }

}