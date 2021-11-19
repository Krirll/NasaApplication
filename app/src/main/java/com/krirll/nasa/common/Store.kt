package com.krirll.nasa.common

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class Store
@Inject constructor():  Serializable {
    fun saveImage(name : String, bitmap: Bitmap, context: Context) {
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            name,
            ""
        )
    }

    fun createName() = UUID.randomUUID().toString()

    fun check(name : String) = MediaStore.Images.Media.getContentUri(name) != null

}