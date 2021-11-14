package com.krirll.nasa

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import java.io.Serializable

class FragmentListener(
    private val store : Main.ImageStore = Store(),
    private val permissionChecker : Main.Permission = PermissionChecker()
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
        if (permissionChecker.checkWriteStoragePermission(context)) {
            val name = store.createName()
            store.saveImage(name, image.toBitmap(), context)
            Toast.makeText(
                context,
                if (store.check(name))
                    context.getString(R.string.success)
                else context.getString(R.string.error_download),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            permissionChecker.getPermission(context)
        }
    }
}