package com.krirll.nasa

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.krirll.nasa.network.PhotoModel
import io.reactivex.Observer

interface Main {

    interface ListenerRecyclerViewItem {
        fun click(item: PhotoModel)
        fun downloadMore()
    }

    interface ObservableListener {
        fun getData(listener : Observer<List<PhotoModel>>)
    }

    interface FragmentListener {
        fun onOpen(view : View)
        fun onClose(view : View)
        fun downloadImage(image : Drawable, context: Context)
    }

    interface ImageStore {
        fun saveImage(name : String, bitmap : Bitmap, context: Context)
        fun createName() : String
        fun check(name : String) : Boolean
    }

    interface Permission {
        fun checkWriteStoragePermission(context : Context) : Boolean
        fun getPermission(context: Context)
    }

}