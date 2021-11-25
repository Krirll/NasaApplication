package com.krirll.nasa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krirll.nasa.common.ListenerRecyclerViewItem
import com.krirll.nasa.R
import com.krirll.nasa.network.PhotoModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var listener : ListenerRecyclerViewItem? = null

    fun setListener(newListener : ListenerRecyclerViewItem) {
        listener = newListener
    }

    fun submitList(newList : List<PhotoModel>) {
        differ.submitList(newList)
    }

    private val differCallback = object : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView? = itemView.findViewById(R.id.title)
        val image : ImageView? = itemView.findViewById(R.id.image)
        val progress : ProgressBar? = itemView.findViewById(R.id.progressDownload)
        val downloadButton : Button? = itemView.findViewById(R.id.downloadMore)
        val more : Button? = itemView.findViewById(R.id.more)
        val downloadImage : ProgressBar? = itemView.findViewById(R.id.imageProgress)
        val errorImage : TextView? = itemView.findViewById(R.id.imageError)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                                     .inflate(
                                         if (viewType == R.layout.recycler_view_item)
                                             R.layout.recycler_view_item
                                         else
                                             R.layout.last_item_recycler_view,
                                         parent,
                                         false
                                     )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == differ.currentList.size) {
            holder.progress?.visibility = View.INVISIBLE
            holder.downloadButton?.visibility = View.VISIBLE
            holder.downloadButton?.setOnClickListener {
                holder.progress?.visibility = View.VISIBLE
                holder.downloadButton.visibility = View.GONE
                listener?.download()
            }
        }
        else {
            holder.downloadImage?.visibility = View.VISIBLE
            holder.errorImage?.visibility = View.GONE
            holder.image?.visibility = View.INVISIBLE
            holder.more?.visibility = View.INVISIBLE
            holder.more?.setOnClickListener {
                listener?.click(differ.currentList[position])
            }
            holder.title?.text = differ.currentList[position].title
            Picasso.get().isLoggingEnabled = true
            Picasso.get()
                .load(
                    if (differ.currentList[position].hdImageUrl == null)
                        differ.currentList[position].imageOfVideoUrl
                    else
                        differ.currentList[position].imageUrl
                )
                .fit()
                .centerCrop()
                .into(holder.image, object : Callback {
                    override fun onSuccess() {
                        holder.downloadImage?.visibility = View.GONE
                        holder.image?.visibility = View.VISIBLE
                        holder.more?.visibility = View.VISIBLE
                    }
                    override fun onError(e: Exception?) {
                        holder.downloadImage?.visibility = View.GONE
                        holder.errorImage?.visibility = View.VISIBLE
                    }
                })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == differ.currentList.size)
            R.layout.last_item_recycler_view
                else
            R.layout.recycler_view_item
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

}