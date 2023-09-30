package com.adilegungor.gungorecommerce.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.adilegungor.gungorecommerce.data.model.ProductEntity

//favoriler için @Database(entities = [ProductEntity::class, ProductEntity::class], version = 1)
//veritabanısınıf:local dbleri yönet.
//[ProductEntity::class] hangi db kull.  tanım
@Database(entities = [ProductEntity::class], version = 1)
//db değiş. sürüm upd. et. vers.

//roomdb local db işl. için.
//kalıtım
abstract class ProductRoomDB : RoomDatabase(){

    abstract fun productsDao(): ProductDao
    //db işlemlerinin nin olduğu daoya erişim
}