package com.adilegungor.gungorecommerce.data.model

data class GetProductsResponse(
    val status: Int?,
    val message: String?,
    val products: List<Product>?//tüm liste(ürün)
)
