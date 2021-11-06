package com.krirll.nasa

import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicassoConverter : Image.Converter {
    override fun push(view: ImageView, url : String) {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .into(view)
    }
}