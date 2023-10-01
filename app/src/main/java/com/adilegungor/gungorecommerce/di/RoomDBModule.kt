package com.adilegungor.gungorecommerce.di

import android.content.Context
import androidx.room.Room
import com.adilegungor.gungorecommerce.data.source.local.ProductRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton

    //aş. @ notasyonu uyg. genel bağlamını (context) alır ve db yi oluşturur.
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProductRoomDB::class.java, "product_room_db").build()
//burada bir product roomdb oluşturuldu
    @Provides
    @Singleton

//aş. fonk. roomdb den dao oluşt. ve döndürür. - dao locale erişir-db etkileşimi
    fun provideDao(roomDB: ProductRoomDB) = roomDB.productsDao()

}