package com.dzy.chiyan.data

data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val sentAt: String
)
