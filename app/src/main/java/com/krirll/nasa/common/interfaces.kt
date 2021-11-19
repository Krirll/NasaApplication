package com.krirll.nasa.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.krirll.nasa.network.PhotoModel


interface ListenerRecyclerViewItem {
        fun click(item: PhotoModel)
        fun download()
    }

interface ViewListener {
    fun onOpen(view: View)
    fun onClose(view: View)
    fun downloadImage(image: Drawable, context: Context)
}
