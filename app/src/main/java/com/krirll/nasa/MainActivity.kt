package com.krirll.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krirll.nasa.network.Photo

class ObservableListener : Main.ObservableListener {
    override fun download(listener: Main.ObservableListener.OnFinishedListener) {
        //todo rxjava + retrofit
    }
}

class MainViewModel : ViewModel(), Main.ListenerRecyclerViewItem, Main.ObservableListener.OnFinishedListener {

    private val model  : Main.ObservableListener = ObservableListener()
    private var photo : MutableLiveData<Photo?> = MutableLiveData(null)

    private var recyclerViewList : MutableLiveData<MutableList<Photo>> = MutableLiveData(mutableListOf())
    private var lastPosition : MutableLiveData<Int> = MutableLiveData(0)

    fun getRecyclerViewList() = recyclerViewList

    fun getLastPosition() = lastPosition

    fun getPhotoForFragment() = photo

    fun downloadPhotos() {
        model.download(this)
    }

    override fun click(item: Photo) {
        photo.value = item
    }

    override fun downloadMore() {
        //todo вызывать модель, брать новые данные, перемещаться к бывшему последнему элементу, по завершению удалять этот last item
        model.download(this)
    }

    override fun onComplete() {
        TODO("Not yet implemented")
    }

    override fun onNext() {
        TODO("Not yet implemented")
    }

    override fun onError() {
        TODO("Not yet implemented")
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val viewModel : MainViewModel by viewModels()
        //todo skeleton
        viewModel.downloadPhotos()
        viewModel.getRecyclerViewList().observe(this, { recyclerView.adapter = RecyclerViewAdapter(it) })
        viewModel.getLastPosition().observe(this, { recyclerView.scrollToPosition(it) })
        viewModel.getPhotoForFragment().observe(this, { if (it != null) startFragment(it) })
    }

    private fun startFragment(photo: Photo) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<WatchFragment>(R.id.fragmentContainer,
                args = bundleOf(WatchFragment.PHOTO to photo))
        }
    }
}