package com.krirll.nasa.di.modules

import com.krirll.nasa.network.RetrofitClient
import dagger.Module
import dagger.Provides
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RepositoryModule {

    @Provides
    fun provideRetrofitClient(
        converter: GsonConverterFactory,
        callAdapter: RxJava2CallAdapterFactory
    ) = RetrofitClient(converter, callAdapter)

    @Provides
    fun provideConverter() : GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideCallAdapter() : RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

}