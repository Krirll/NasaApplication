package com.krirll.nasa.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.krirll.nasa.*
import com.krirll.nasa.network.PhotoModel

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
            if (it != null) {
                showDialog(
                    getString(R.string.check_internet),
                    getString(R.string.connection_error),
                    if (adapter.itemCount - 1 == 0)
                        this
                    else
                        null
                )
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.downloadMore()
    }

    private fun showDialog(message : String, title : String, activity : MainActivity? = null) {
        val dialog = AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(false)
            setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss(); if (activity != null) finish() }
        }
        val alertError = dialog.create()
        alertError.setTitle(title)
        alertError.show()
    }

    private fun startFragment(photoModel: PhotoModel?) {
        if (photoModel != null) {
            val listener = FragmentListener()
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<WatchFragment>(
                    R.id.fragmentContainer,
                    args = bundleOf(
                        WatchFragment.PHOTO to photoModel,
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
                        getString(R.string.permission_message),
                        getString(R.string.application)
                    )
                }
        }
    }

    companion object {
        const val REQ_CODE = 1
    }
}