package com.adilegungor.gungorecommerce.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adilegungor.gungorecommerce.data.model.ProductEntity


@Dao //sınıf:roomdao
interface ProductDao {

    @Query("SELECT * FROM cart_products")//get all pro
    suspend fun getProducts(): List<ProductEntity>
    //product entity veri listesi
     //askıya alınır işlem- tüm ürünleri al.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //onflict:çakışma=>yeniyi ekle
    suspend fun addProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}