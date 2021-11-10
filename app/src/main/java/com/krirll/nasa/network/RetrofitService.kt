package com.krirll.nasa.network

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {

    @GET("apod?count=20&api_key=i9jxG8dOwfLh0kqaQKqGA6SGwdO7pqEM51IIvl0k&thumbs=true")
    fun getPosts() : Observable<List<PhotoModel>>

    companion object {
        fun factory() : RetrofitService {
            return Retrofit.Builder()
                           .baseUrl("https://api.nasa.gov/planetary/")
                           .addConverterFactory(GsonConverterFactory.create())
                           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                           .build()
                           .create(RetrofitService::class.java)
        }
    }
}