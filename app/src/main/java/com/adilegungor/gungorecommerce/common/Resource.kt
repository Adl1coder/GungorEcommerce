package com.adilegungor.gungorecommerce.common

//  kaynak sınıfı
sealed class Resource<out T : Any> {
    // Başarılı durum - alt sınıf
    data class Success<out T : Any>(val data: T) : Resource<T>()

    // Hata durumu - alt sınıf
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}
