package com.krirll.nasa.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GetData {
    @GET("apod?api_key=i9jxG8dOwfLh0kqaQKqGA6SGwdO7pqEM51IIvl0k")
    fun getBestDailyPhoto() : Single<Photo>

    @GET("apod?count=20&api_key=i9jxG8dOwfLh0kqaQKqGA6SGwdO7pqEM51IIvl0k&thumbs=true")
    fun getPosts() : Observable<List<Photo>>

    companion object {
        fun factory() : GetData {
            return Retrofit.Builder()
                           .baseUrl("https://api.nasa.gov/planetary/")
                           .addConverterFactory(GsonConverterFactory.create())
                           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                           .build()
                           .create(GetData::class.java)
        }
    }
}