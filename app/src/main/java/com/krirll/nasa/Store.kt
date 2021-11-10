package com.krirll.nasa

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.Serializable
import java.util.*

class Store : Main.ImageStore, Serializable {
    override fun saveImage(name : String, bitmap: Bitmap, context: Context) {
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            name,
            ""
        )
    }

    override fun createName() = UUID.randomUUID().toString()

    override fun check(name : String) = MediaStore.Images.Media.getContentUri(name) != null

}