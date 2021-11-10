package com.krirll.nasa

import com.krirll.nasa.network.RetrofitService
import com.krirll.nasa.network.PhotoModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ObservableListener : Main.ObservableListener {
    override fun getData(listener: Observer<List<PhotoModel>>) {
        val retrofitService = RetrofitService.factory()
        retrofitService.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener)
    }
}