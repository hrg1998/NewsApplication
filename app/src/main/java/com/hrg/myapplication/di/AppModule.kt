package com.hrg.myapplication.di

import android.content.Context
import androidx.room.Room
import com.hrg.myapplication.data.local.NewsDB
import com.hrg.myapplication.data.local.dao.NewsDao
import com.hrg.myapplication.data.remote.ApiService
import com.hrg.myapplication.data.remote.impl.NewsDataSource
import com.hrg.myapplication.data.remote.source.NewsDataSourceImpl
import com.hrg.myapplication.data.repository.impl.NewsRepository
import com.hrg.myapplication.data.repository.source.NewsRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideNewsDataSource(newsDataSource: NewsDataSourceImpl):
            NewsDataSource = newsDataSource

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): NewsDB {
        return Room.databaseBuilder(
            context, NewsDB::class.java, NewsDB.databaseName
        ).build()
    }

    @Provides
    fun getNewsDao(appDatabase: NewsDB): NewsDao {
        return appDatabase.NewsDao()
    }

    @Provides
    fun provideNewsRepo(newsRepo: NewsRepositoryImpl):
            NewsRepository = newsRepo
}