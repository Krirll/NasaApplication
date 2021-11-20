package com.krirll.nasa.ui

import android.os.Bundle
import android.view.View
import com.krirll.nasa.network.PhotoModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.krirll.nasa.common.FragmentListener
import com.krirll.nasa.R

class WatchFragment : Fragment(R.layout.fragment_watch) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = requireArguments().getSerializable(PHOTO) as PhotoModel
        val image = view.findViewById<ImageView>(R.id.imageFragment)
        view.findViewById<TextView>(R.id.titleFragment).text = photo.title
        Picasso.get().isLoggingEnabled = true
        Picasso.get()
            .load(photo.hdImageUrl ?: photo.imageOfVideoUrl)
            .into(image, object  : Callback {
                override fun onSuccess() {
                    view.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.INVISIBLE
                }

                override fun onError(e: Exception?) {
                    view.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.GONE
                    view.findViewById<TextView>(R.id.imageError).visibility = View.VISIBLE
                }

            })
        view.findViewById<TextView>(R.id.description).text = photo.explanation
        view.findViewById<TextView>(R.id.author).text = getString(R.string.author, photo.author ?: "Unknown")
        view.findViewById<TextView>(R.id.date).text = getString(R.string.date, photo.date)
        val listener = requireArguments().getSerializable(CLOSE_LISTENER) as FragmentListener
        view.findViewById<Button>(R.id.closeButton).setOnClickListener {
            listener.onClose(view.parent.parent as CardView)
        }
        view.findViewById<Button>(R.id.download).setOnClickListener {
            if (image.drawable != null)
                listener.downloadImage(image.drawable, view.context)
            else
                Toast.makeText(
                    view.context,
                    getString(R.string.error_download),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    companion object {
        const val PHOTO = "PHOTO"
        const val CLOSE_LISTENER = "CLOSE_LISTENER"
    }
}