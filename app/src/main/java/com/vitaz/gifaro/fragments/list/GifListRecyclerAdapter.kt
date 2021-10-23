package com.vitaz.gifaro.fragments.list

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vitaz.gifaro.MainApplication
import com.vitaz.gifaro.R
import com.vitaz.gifaro.database.tables.favourite.Favourite
import com.vitaz.gifaro.databinding.GifListItemRowBinding
import com.vitaz.gifaro.misc.getFrescoProgressBarLoadable
import com.vitaz.gifaro.misc.setUri
import com.vitaz.gifaro.networking.dto.GifObject

class GifListRecyclerAdapter (
    val context: Context
): RecyclerView.Adapter<GifListRecyclerAdapter.GifItemHolder>() {
    private var listener: OnGifSelectListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var gifList: MutableList<GifObject> = mutableListOf()
    private var favouriteList: MutableList<String> = mutableListOf()
    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = Resources.getSystem().displayMetrics.widthPixels

    internal fun setGifs(gifList: List<GifObject>?) {
        this.gifList.removeAll(this.gifList)
        if (gifList != null) {
            this.gifList.addAll(gifList)
        }
        notifyDataSetChanged()
    }

    internal fun getListOfFavourites(): MutableList<String> {
        return favouriteList
    }

    internal fun setFavourites(favouriteList: List<Favourite>) {
        if (favouriteList.size > this.favouriteList.size) {
            addFavourites(favouriteList)
        } else {
            deleteFavourites(favouriteList)
        }
    }

    private fun deleteFavourites(favouriteList: List<Favourite>) {
        val idsToDelete = this.favouriteList.filter { it !in favouriteList.map{it.id} }
        for (id in idsToDelete) {
            this.favouriteList.remove(id)
        }
        for (item in gifList) {
            if (item.id in idsToDelete) {
                notifyItemChanged(gifList.indexOf(item))
            }
        }
    }

    private fun addFavourites(favouriteList: List<Favourite>) {
        //Add only new favourite items to the list and update corresponding holders
        val newItems = favouriteList.filter { it.id !in this.favouriteList }.map{it.id}
        this.favouriteList.addAll(newItems)
        for (item in gifList) {
            if (item.id in newItems) {
                notifyItemChanged(gifList.indexOf(item))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GifItemHolder(GifListItemRowBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: GifItemHolder, position: Int) =
        holder.bindGifs(gifList[position])

    override fun getItemCount() = gifList.size

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

            // Fresco cannot work with wrap_content properly, so we just have to stretch the height manually to keep image proportions
            val multiplicator =  width / gif.images.original.width
            val desiredHeight = gif.images.original.height * multiplicator
            view.image.layoutParams.height = desiredHeight
            view.image.layoutParams.width = width

            val uri = Uri.parse(gif.images.original.url)
            view.image.hierarchy.setProgressBarImage(getFrescoProgressBarLoadable())
            setUri(view.image, uri, true);

            // Set favourite button:
            if (gif.id in favouriteList) {
                view.favouriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainApplication.instance.getAppContext()!!,
                        R.drawable.ic_baseline_favorite_24
                    )
                )
            } else {
                view.favouriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        MainApplication.instance.getAppContext()!!,
                        R.drawable.ic_baseline_non_favorite_24
                    )
                )
            }
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
