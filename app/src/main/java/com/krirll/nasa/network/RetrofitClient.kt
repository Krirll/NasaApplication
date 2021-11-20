package com.krirll.nasa.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient
@Inject constructor(
    private val converter : GsonConverterFactory,
    private val callAdapter : RxJava2CallAdapterFactory
) {

    fun getRetrofitClient(url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(converter)
            .addCallAdapterFactory(callAdapter)
            .build()

}