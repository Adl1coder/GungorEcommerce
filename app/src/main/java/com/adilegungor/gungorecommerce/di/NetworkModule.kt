package com.adilegungor.gungorecommerce.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.adilegungor.gungorecommerce.common.Constants.BASE_URL
import com.adilegungor.gungorecommerce.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT = 60L

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context:Context) =  ChuckerInterceptor.Builder(context).build()
     //chucker ı burada kullandım. Ağ isteklerini uygulama çalışma esnasında izlemek için.
    @Provides
    @Singleton
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .addHeader("store", "canerture")
                .build()
            chain.proceed(modifiedRequest)
        }
         //addıntercopter ile ağ isteklerine özel başlık ekkl.
        addInterceptor(chuckerInterceptor)

         //zaman aşımı süreleri belirleniyor
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit) = retrofit.create<ProductService>()

}