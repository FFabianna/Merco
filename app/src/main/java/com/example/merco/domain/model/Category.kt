package com.example.merco.domain.model

import android.net.Uri
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable

data class Category(
    val id: String="",
    val name: String="",
    val imageId: String = ""
)

