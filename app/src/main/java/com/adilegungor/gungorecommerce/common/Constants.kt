package com.adilegungor.gungorecommerce.common

object Constants {
    //temel url
    const val BASE_URL = "https://api.canerture.com/ecommerce/"

    object Endpoint {
        //istekleri getiren endpoint noktalarımız
        const val ADD_CART_PRODUCTS = "add_to_cart.php"
        const val DELETE_CART_PRODUCTS = "delete_from_cart.php"
        const val GET_PRODUCTS = "get_products.php"
        const val GET_PRODUCT_DETAIL = "get_product_detail.php"
        const val GET_SALE_PRODUCTS = "get_sale_products.php"//indirimdekiler
        const val CLEAR_CART_PRODUCTS = "clear_cart.php"
        const val GET_PRODUCTS_BY_CATEGORY = "get_products_by_category.php"
        const val GET_CART_PRODUCTS = "get_cart_products.php"
        const val GET_SEARCH_PRODUCT = "search_product.php"


    }
}