package com.aravind.zohotask.di

import android.content.Context
import com.aravind.zohotask.Application
import com.aravind.zohotask.database.AppDatabase
import com.aravind.zohotask.database.NewsDao
import com.aravind.zohotask.network.NewsService
import com.aravind.zohotask.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

 
    @Provides
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

     
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(loggingInterceptor).build()
    }

     
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

     
    @Provides
    fun apiService(retrofit: Retrofit) : NewsService{
        return retrofit.create(NewsService::class.java)
    }

     
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) :AppDatabase{
        return AppDatabase.getAppDbInstance(context)
    }

     
    @Provides
    fun provideDao(appDatabase: AppDatabase) : NewsDao{
        return appDatabase.getDao()
    }
}