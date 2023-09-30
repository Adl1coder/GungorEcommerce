package com.adilegungor.gungorecommerce.data.model
//seçilen ürün detayı
data class GetProductDetailResponse(
    val status: Int?,
    val message: String?,
    val product: Product? //nesne
)
