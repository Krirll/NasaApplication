package com.krirll.nasa.network

class RetrofitService private constructor() {

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/planetary/"
        fun getRetrofitService() : Service =
            RetrofitClient.getRetrofitClient(BASE_URL).create(Service::class.java)
    }

}