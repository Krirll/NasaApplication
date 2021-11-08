package com.krirll.nasa

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.krirll.nasa.network.GetData
import com.krirll.nasa.network.Photo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import java.util.*

class ObservableListener : Main.ObservableListener {
    override fun download(listener: Observer<List<Photo>>) {
        val retrofitService = GetData.factory()
        retrofitService.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener)
    }
}

class MainViewModel(
    private val model  : Main.ObservableListener = ObservableListener()
) : ViewModel(), Main.ListenerRecyclerViewItem {

    private val listenerObserver = (
            object : Observer<List<Photo>> {
                override fun onComplete() {}

                override fun onNext(photoList: List<Photo>) {
                    val newList = recyclerViewList.value?.plus(photoList)
                    recyclerViewList.value = newList?.toMutableList()
                    lastPosition.value = 20 * countRequests
                    countRequests++
                }

                override fun onError(e: Throwable) {
                    onErrorAction.value = true
                }

                override fun onSubscribe(d: Disposable) {}
            }
            )

    private var photo : MutableLiveData<Photo?> = MutableLiveData(null)
    private var onErrorAction : MutableLiveData<Boolean?> = MutableLiveData(null)
    private var recyclerViewList : MutableLiveData<MutableList<Photo>> = MutableLiveData(mutableListOf())
    private var lastPosition : MutableLiveData<Int> = MutableLiveData(0)
    private var countRequests = 0

    fun getRecyclerViewList() = recyclerViewList
    fun getLastPosition() = lastPosition
    fun getPhotoForFragment() = photo
    fun getOnErrorAction() = onErrorAction

    override fun click(item: Photo) {
        photo.value = item
    }

    override fun downloadMore() {
        model.download(listenerObserver)
    }

    fun download() {
        model.download(listenerObserver)
    }
}

class MainActivity : AppCompatActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shimmerLayout = findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout)
        shimmerLayout.startShimmerAnimation()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        val viewModel : MainViewModel by viewModels()
        adapter.setListener(viewModel)
        viewModel.getRecyclerViewList().observe(this, {
            if (it.isNotEmpty()) {
                shimmerLayout.stopShimmerAnimation()
                shimmerLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.setList(it.toList())
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getLastPosition().observe(this, { recyclerView.scrollToPosition(it) })
        viewModel.getPhotoForFragment().observe(this, {
                startFragment(it)
        })
        viewModel.getOnErrorAction().observe(this, {
            if (it != null)
                showDialog(
                    adapter.itemCount - 1 == 0,
                    "Check your internet connection",
                    "CONNECTION ERROR"
                )
        })
        viewModel.download()
    }

    private fun showDialog(isEmpty : Boolean, message : String, title : String) {
        val dialog = AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss(); if (isEmpty) finish() }
        }
        val alertError = dialog.create()
        alertError.setTitle(title)
        alertError.show()
    }

    private fun startFragment(photo: Photo?) {
        if (photo != null) {
            val listener = Fragment()
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<WatchFragment>(
                    R.id.fragmentContainer,
                    args = bundleOf(
                        WatchFragment.PHOTO to photo,
                        WatchFragment.CLOSE_LISTENER to listener
                    )
                )
            }
            listener.onOpen(findViewById(R.id.cardFragment))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_CODE ->
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showDialog(
                        false,
                        "the application needs access to the storage so that you can save images",
                        "Application")
                }
        }
    }

    companion object {
        const val REQ_CODE = 1
    }
}

class Fragment : Main.FragmentListener, Serializable {
    fun onOpen(cardView : CardView) {
        val animationUp =
            TranslateAnimation(0f, 0f, cardView.height.toFloat(), 0f)
        animationUp.duration = 800
        animationUp.interpolator = AnticipateOvershootInterpolator()
        cardView.visibility = View.VISIBLE
        cardView.startAnimation(animationUp)
    }

    override fun onClose(cardView : CardView) {
        val animationDown =
            TranslateAnimation(0f, 0f, 0f, cardView.height.toFloat())
        animationDown.duration = 700
        animationDown.interpolator = AnticipateOvershootInterpolator()
        cardView.startAnimation(animationDown)
        cardView.visibility = View.INVISIBLE
    }

    override fun downloadImage(image: Drawable, context : Context) {
        val status = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (status == PackageManager.PERMISSION_GRANTED) {
            val name = UUID.randomUUID().toString()
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                image.toBitmap(),
                name,
                ""
            )
            Toast.makeText(
                context,
                if (MediaStore.Images.Media.getContentUri(name) != null)
                    "Download success"
                else "Error while downloading image",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MainActivity.REQ_CODE
            )
        }
    }

}