package com.krirll.nasa.repository

import com.krirll.nasa.network.PhotoModel
import com.krirll.nasa.network.RetrofitService
import io.reactivex.Observable

class Repository(private val service : RetrofitService) {
    fun getObserverModelObserver() : Observable<List<PhotoModel>> = service.getPosts()
}