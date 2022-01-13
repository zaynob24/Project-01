package com.example.socialmedia.model

import java.util.*

data class Posts(

    val postMassage: String = "",
    val imageUrl: String = "",
    val imageName: String = "",
    val userId: String = "",
    var userName:String="",
    val postId: String = UUID.randomUUID().toString()

)
