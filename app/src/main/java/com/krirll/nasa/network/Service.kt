package com.krirll.nasa.network

import io.reactivex.Observable
import retrofit2.http.GET

interface Service {

    @GET("apod?count=20&api_key=i9jxG8dOwfLh0kqaQKqGA6SGwdO7pqEM51IIvl0k&thumbs=true")
    fun getPosts() : Observable<List<PhotoModel>>

}