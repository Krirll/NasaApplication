package com.krirll.nasa

import com.krirll.nasa.network.Photo
import io.reactivex.Observer

interface Main {

    interface ListenerRecyclerViewItem {
        fun click(item: Photo)
        fun downloadMore()
    }

    interface ObservableListener {
        fun download(listener : Observer<List<Photo>>)
    }

    interface FragmentListener {
        fun onClose()
    }

}