package com.adilegungor.gungorecommerce.di

import com.adilegungor.gungorecommerce.data.repository.ProductRepository
import com.adilegungor.gungorecommerce.data.source.local.ProductDao
import com.adilegungor.gungorecommerce.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(productService: ProductService, productDao: ProductDao) : ProductRepository =
        ProductRepository(productService, productDao)

}