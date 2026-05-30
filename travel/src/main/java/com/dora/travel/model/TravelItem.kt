package com.dora.travel.model

data class TravelItem(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val rating: Float,
    val location: String
)

data class TravelCategory(
    val id: Int,
    val name: String,
    val iconRes: Int? = null
)
