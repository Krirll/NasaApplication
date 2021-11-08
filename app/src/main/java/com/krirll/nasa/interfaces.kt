package com.krirll.nasa

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.cardview.widget.CardView
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
        fun onClose(cardView : CardView)
        fun downloadImage(image : Drawable, context: Context)
    }

}