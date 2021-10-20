package com.vitaz.gifaro.fragments.list

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.vitaz.gifaro.databinding.GifListItemRowBinding
import com.vitaz.gifaro.networking.dto.GifObject

class GifListRecyclerAdapter (
    val context: Context
): RecyclerView.Adapter<GifListRecyclerAdapter.GifItemHolder>() {
    private var listener: OnGifSelectListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var gifList: MutableList<GifObject> = mutableListOf()
    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = Resources.getSystem().displayMetrics.widthPixels

    internal fun setGifs(gifList: List<GifObject>?) {
        this.gifList.removeAll(this.gifList)
        if (gifList != null) {
            this.gifList.addAll(gifList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GifItemHolder(GifListItemRowBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: GifItemHolder, position: Int) =
        holder.bindGifs(gifList[position])

    override fun getItemCount() = gifList.size

    fun removeItem(position: Int) {
        gifList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItemAtPosition(position: Int): GifObject {
        return gifList[position]
    }

    inner class GifItemHolder(
        private val view: GifListItemRowBinding
    ) : RecyclerView.ViewHolder(view.root), View.OnClickListener {

        private lateinit var gif: GifObject

        init {
            view.favouriteButton.setOnClickListener(this)
        }

        fun bindGifs(gif: GifObject) {

            view.gif = gif
            this.gif = gif

            // DraweeView cannot be set to wrap_content, so we just have to stretch the height manually to keep image proportions
            val multiplicator =  width / gif.images.original.width
            val desiredHeight = gif.images.original.height * multiplicator
            view.image.layoutParams.height = desiredHeight
            view.image.layoutParams.width = width

            val uri = Uri.parse(gif.images.original.url)
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build()
            view.image.controller = controller

            Log.d("GIF URI RECEIVED", gif.images.original.url.toString())

        }

        override fun onClick(p0: View?) {
            listener?.onGifSelect(this.gif)
        }
    }

    interface OnGifSelectListener {
        fun onGifSelect(gif: GifObject)
    }

    fun setGifSelectListener(listener: OnGifSelectListener) {
        this.listener = listener
    }
}
