package com.example.takehome


import LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun provideLocalDataSource(): LocalDataSource {
//        return LocalDataSourceImpl()  // Your LocalDataSource implementation
//    }
}