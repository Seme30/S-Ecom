package com.semeprojects.secom.data.network.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null
)