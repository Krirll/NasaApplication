package com.krirll.nasa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krirll.nasa.network.Photo

class RecyclerViewAdapter(
    private val list : List<Photo>,
    private val listener : Main.ListenerRecyclerViewItem = MainViewModel(),
    private val imageConverter : Image.Converter = PicassoConverter()
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView? = itemView.findViewById(R.id.title)
        val image : ImageView? = itemView.findViewById(R.id.image)
        val progress : ProgressBar? = itemView.findViewById(R.id.progressDownload)
        val downloadButton : Button? = itemView.findViewById(R.id.downloadMore)
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
        if (position == list.size) {
            holder.downloadButton?.setOnClickListener {
                holder.progress?.visibility = View.VISIBLE

                listener.downloadMore()
                //todo запуск загрузки новых фото
                //recyclerView.scrollToPosition(your_position)
            }
        }
        else {
            holder.title?.text = list[position].title
            imageConverter.push(holder.image!!, list[position].imageUrl!!)
            holder.itemView.setOnClickListener {
                listener.click(list[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size)
                    R.layout.last_item_recycler_view
                else
                    R.layout.recycler_view_item
    }

    override fun getItemCount(): Int = list.size + 1

}