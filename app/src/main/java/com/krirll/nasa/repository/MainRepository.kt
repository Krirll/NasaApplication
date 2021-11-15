package com.krirll.nasa.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.krirll.nasa.network.PhotoModel
import com.krirll.nasa.network.RetrofitService
import com.krirll.nasa.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainRepository (
    private val retrofitService: Service = RetrofitService.getRetrofitService()
) {
    private val compositeDisposable = CompositeDisposable()

    private var onErrorAction : MutableLiveData<Boolean?> = MutableLiveData(null)
    private val getPhotoLiveData : MutableLiveData<MutableList<PhotoModel>> = MutableLiveData(mutableListOf())

    fun getPhotoLiveData() : LiveData<MutableList<PhotoModel>> {
            compositeDisposable.add(
                retrofitService.getPosts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { photoModels ->
                        if (photoModels != null)
                            getPhotoLiveData.value =
                                getPhotoLiveData.value?.plus(photoModels)?.toMutableList()
                        else
                            onErrorAction.value = true
                    }
            )
            return getPhotoLiveData
        }

    fun getOnErrorAction() = onErrorAction
}