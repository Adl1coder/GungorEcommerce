package com.adilegungor.gungorecommerce.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.adilegungor.gungorecommerce.data.model.ProductEntity

//favoriler için @Database(entities = [ProductEntity::class, ProductEntity::class], version = 1)
@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductRoomDB : RoomDatabase(){

    abstract fun productsDao(): ProductDao
}