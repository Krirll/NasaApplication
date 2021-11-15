package com.krirll.nasa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krirll.nasa.network.PhotoModel
import com.krirll.nasa.repository.MainRepository

class MainViewModel(
    private val repository: MainRepository = MainRepository()
) : ViewModel(), ListenerRecyclerViewItem {

    private var photoModel : MutableLiveData<PhotoModel?> = MutableLiveData(null)

    fun getPhotoForFragment() = photoModel

    fun getOnErrorAction() = repository.getOnErrorAction()

    override fun click(item: PhotoModel) {
        photoModel.value = item
    }

    override fun download() = repository.getPhotoLiveData()
}