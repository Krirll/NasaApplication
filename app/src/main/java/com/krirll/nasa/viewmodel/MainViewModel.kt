package com.krirll.nasa.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krirll.nasa.common.ListenerRecyclerViewItem
import com.krirll.nasa.di.components.DaggerRepositoryComponent
import com.krirll.nasa.di.modules.RetrofitModule
import com.krirll.nasa.network.PhotoModel
import com.krirll.nasa.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private var repository: Repository =
        DaggerRepositoryComponent.builder()
            .retrofitModule(RetrofitModule())
            .build()
            .getRepository()
) : ViewModel(), ListenerRecyclerViewItem {

    private val compositeDisposable = CompositeDisposable()
    private val listPhotoModel : MutableLiveData<MutableList<PhotoModel>> = MutableLiveData(mutableListOf())
    private val onErrorAction : MutableLiveData<Boolean> = MutableLiveData(false)
    private val photoModel : MutableLiveData<PhotoModel?> = MutableLiveData(null)

    fun getOnErrorAction() = onErrorAction
    fun getListPhotoModel() : MutableLiveData<MutableList<PhotoModel>> {
        getPhotos()
        return listPhotoModel
    }
    fun getPhotoForFragment() = photoModel

    private fun getPhotos() {
        compositeDisposable.add(
        repository.getRemoteService()
            .getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it != null)
                        listPhotoModel.value = listPhotoModel.value?.plus(it)?.toMutableList()
                },
                {
                    onErrorAction.value = true
                }
            )
        )
    }

    override fun click(item: PhotoModel) {
        photoModel.value = item
    }

    override fun download() {
        getPhotos()
    }
}