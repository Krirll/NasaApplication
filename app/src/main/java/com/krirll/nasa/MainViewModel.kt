package com.krirll.nasa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krirll.nasa.network.PhotoModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainViewModel(
    private val model  : Main.ObservableListener = ObservableListener()
) : ViewModel(), Main.ListenerRecyclerViewItem {

    private val listenerObserver = (
            object : Observer<List<PhotoModel>> {
                override fun onComplete() {}

                override fun onNext(photoModelList: List<PhotoModel>) {
                    val newList = recyclerViewList.value?.plus(photoModelList)
                    recyclerViewList.value = newList?.toMutableList()
                    lastPosition.value = 20 * countRequests
                    countRequests++
                }

                override fun onError(e: Throwable) {
                    onErrorAction.value = true
                }

                override fun onSubscribe(d: Disposable) {}
            }
            )

    private var photoModel : MutableLiveData<PhotoModel?> = MutableLiveData(null)
    private var onErrorAction : MutableLiveData<Boolean?> = MutableLiveData(null)
    private var recyclerViewList : MutableLiveData<MutableList<PhotoModel>> = MutableLiveData(mutableListOf())
    private var lastPosition : MutableLiveData<Int> = MutableLiveData(0)
    private var countRequests = 0

    fun getRecyclerViewList() = recyclerViewList
    fun getLastPosition() = lastPosition
    fun getPhotoForFragment() = photoModel
    fun getOnErrorAction() = onErrorAction

    override fun click(item: PhotoModel) {
        photoModel.value = item
    }

    override fun downloadMore() {
        model.getData(listenerObserver)
    }
}