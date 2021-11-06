package com.krirll.nasa

import android.widget.ImageView
import com.krirll.nasa.network.Photo

interface Main {

    interface ListenerRecyclerViewItem {
        fun click(item: Photo)
        fun downloadMore()
    }

    interface ObservableListener {

        interface OnFinishedListener {
            fun onComplete()
            fun onNext()
            fun onError()
        }
        fun download(listener : OnFinishedListener)
    }

}

interface Image {

    interface Converter {
        fun push(view : ImageView, url : String)
    }

}