package com.adilegungor.gungorecommerce.data.model
//JSON formatına göre classlar.
data class AddToCartRequest(
    val userId: String,
    val productId: Int
)