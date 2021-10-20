package com.vitaz.gifaro.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vitaz.gifaro.databinding.GifListItemRowBinding
import com.vitaz.gifaro.networking.dto.GifObject

class GifListRecyclerAdapter (
    val context: Context
): RecyclerView.Adapter<GifListRecyclerAdapter.GifItemHolder>() {
    private var listener: OnGifSelectListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var gifList: MutableList<GifObject> = mutableListOf()

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
            view.root.setOnClickListener(this)
        }

        fun bindGifs(gif: GifObject) {
            view.gif = gif

//            val uri = Uri.parse(recipe.image)
//            val request = ImageRequest.fromUri(uri)
//            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setOldController(view.recipeImage.controller).build()
//            view.recipeImage.controller = controller
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
