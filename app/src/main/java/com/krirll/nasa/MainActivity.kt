package com.krirll.nasa

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.animation.AnimationUtils
import com.krirll.nasa.network.GetData
import com.krirll.nasa.network.Photo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

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
                    recyclerViewList.value = photoList as MutableList<Photo>
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

    fun getRecyclerViewList() = recyclerViewList
    fun getLastPosition() = lastPosition
    fun getPhotoForFragment() = photo
    fun getOnErrorAction() = onErrorAction

    override fun click(item: Photo) {
        photo.value = item
    }

    override fun downloadMore() {
        //todo брать новые фотки,
        // перемещаться к бывшему последнему элементу,
        // по завершению удалять этот last item (под вопросом)
        //set last position to scroll
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
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getLastPosition().observe(this, { recyclerView.scrollToPosition(it) })
        viewModel.getPhotoForFragment().observe(this, {
                startFragment(it)
        })
        viewModel.getOnErrorAction().observe(this, {
            if (it != null)
                showDialog(adapter.itemCount - 1 == 0)
        })
        viewModel.download()
    }

    private fun showDialog(isEmpty : Boolean) {
        val dialog = AlertDialog.Builder(this).apply {
            setMessage("Check your internet connection")
            setCancelable(false)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss(); if (isEmpty) finish() }
        }
        val alertError = dialog.create()
        alertError.setTitle("CONNETION ERROR")
        alertError.show()
    }

    private fun startFragment(photo: Photo?) {
        if (photo != null) {
            val listener = Fragment(findViewById(R.id.cardFragment))
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<WatchFragment>(
                    R.id.fragmentContainer,
                    args = bundleOf(WatchFragment.PHOTO to photo, WatchFragment.CLOSE_LISTENER to listener)
                )
            }
            listener.onOpen()
        }
    }
}

class Fragment(private var cardView : CardView) : Main.FragmentListener, Serializable {
    fun onOpen() {
        cardView.visibility = View.VISIBLE
    }

    override fun onClose() {
        //todo анимация перемещения вниз для CardView + убрать затемнение
        cardView.visibility = View.GONE
    }

}