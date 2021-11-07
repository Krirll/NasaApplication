package com.krirll.nasa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.krirll.nasa.network.Photo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class WatchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = requireArguments().getSerializable(PHOTO) as Photo
        view.findViewById<TextView>(R.id.titleFragment).text = photo.title
        Picasso.get().isLoggingEnabled = true
        Picasso.get()
            .load(photo.imageUrl)
            .fit()
            .centerCrop()
            .into(view.findViewById(R.id.imageFragment), object  : Callback {
                override fun onSuccess() {
                    view.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.INVISIBLE
                }

                override fun onError(e: Exception?) {
                    view.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.GONE
                    view.findViewById<TextView>(R.id.imageError).visibility = View.VISIBLE
                }

            })
        view.findViewById<TextView>(R.id.description).text = photo.explanation
        view.findViewById<TextView>(R.id.author).text = getString(R.string.author, photo.author)
        view.findViewById<TextView>(R.id.date).text = getString(R.string.date, photo.date)
        val listener = requireArguments().getSerializable(CLOSE_LISTENER) as Main.FragmentListener
        view.findViewById<Button>(R.id.closeButton).setOnClickListener {
            listener.onClose()
        }
        view.findViewById<Button>(R.id.download).setOnClickListener {
            //todo скачать в галерею
            //todo спрашивать разрешение на сохранение
        }
    }

    companion object {
        const val PHOTO = "PHOTO"
        const val CLOSE_LISTENER = "CLOSE_LISTENER"
    }
}