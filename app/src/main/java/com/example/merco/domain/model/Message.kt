package com.example.merco.domain.model

import com.google.firebase.Timestamp

data class Message(
    var id:String = "",
    var content:String = "",
    var date: Timestamp = Timestamp.now(),
    var imageID:String? = null,
    var imageURL:String? = null
)