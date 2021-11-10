package com.krirll.nasa

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.Serializable

class FragmentListener(
    private val store : Main.ImageStore = Store()
) : Main.FragmentListener, Serializable {
    override fun onOpen(view: View) {
        val animationUp =
            TranslateAnimation(0f, 0f, (view as CardView).height.toFloat(), 0f)
        animationUp.duration = 800
        animationUp.interpolator = AnticipateOvershootInterpolator()
        view.visibility = View.VISIBLE
        view.startAnimation(animationUp)
    }

    override fun onClose(view: View) {
        val animationDown =
            TranslateAnimation(0f, 0f, 0f, (view as CardView).height.toFloat())
        animationDown.duration = 700
        animationDown.interpolator = AnticipateOvershootInterpolator()
        view.startAnimation(animationDown)
        view.visibility = View.INVISIBLE
    }

    override fun downloadImage(image: Drawable, context : Context) {
        val status = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (status == PackageManager.PERMISSION_GRANTED) {
            val name = store.createName()
            store.saveImage(name, image.toBitmap(), context)
            Toast.makeText(
                context,
                if (store.check(name))
                    "Download success"
                else "Error while downloading image",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MainActivity.REQ_CODE
            )
        }
    }
}