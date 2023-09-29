package com.adilegungor.gungorecommerce.data.model

data class GetProductsResponse(
    val status: Int?,
    val message: String?,
    val products: List<Product>?//birden fazla ürünün listesi
)
