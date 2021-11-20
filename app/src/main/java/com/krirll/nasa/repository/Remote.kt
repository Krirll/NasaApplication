package com.krirll.nasa.repository

import com.krirll.nasa.network.RetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Remote
@Inject constructor(
    private val retrofitService: RetrofitService
) {
    fun getService() = retrofitService.getRetrofitService()
}
