package com.krirll.nasa.network

import javax.inject.Inject

class RetrofitService
@Inject constructor(
    private val retrofitClient : RetrofitClient
) {
    private val url = "https://api.nasa.gov/planetary/"
    fun getRetrofitService(): Service =
        retrofitClient.getRetrofitClient(url)
                        .create(Service::class.java)

}