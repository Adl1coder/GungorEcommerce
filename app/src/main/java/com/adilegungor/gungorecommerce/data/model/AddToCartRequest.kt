package com.adilegungor.gungorecommerce.data.model
//JSON formatları incelenerek classlar oluşturuldu.
data class AddToCartRequest(
    val userId: String,
    val productId: Int
)