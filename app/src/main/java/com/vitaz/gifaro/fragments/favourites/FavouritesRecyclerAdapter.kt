package com.vitaz.gifaro.fragments.favourites

import android.R
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vitaz.gifaro.database.tables.favourite.Favourite
import com.vitaz.gifaro.databinding.FavouritesItemRowBinding
import com.vitaz.gifaro.misc.getFrescoProgressBarLoadable
import com.vitaz.gifaro.misc.setUri


class FavouritesRecyclerAdapter(
    val context: Context
): RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteItemHolder>() {
    private var listener: FavouritesRecyclerAdapter.OnFavouriteSelectListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = Resources.getSystem().displayMetrics.widthPixels
    private var favouriteList: MutableList<Favourite> = mutableListOf()

    internal fun setFavourites(favouriteList: List<Favourite>?) {
        if (favouriteList?.size == this.favouriteList.size - 1) {
            // Triggered only when item is being deleted.
            // We need it to keep the remove animation. Otherwise notifyDataSetChanged will interrupt it.
            val itemToDelete = this.favouriteList.map {it}.toMutableList()
            itemToDelete.removeAll(favouriteList)
            val positionOfItemToDelete = this.favouriteList.indexOf(itemToDelete.firstOrNull())
            removeItem(positionOfItemToDelete)
        }else if (favouriteList != null) {
            // Initial list filling and adding new items. Added from separate fragment so no harm to animations
            this.favouriteList.removeAll(this.favouriteList)
            this.favouriteList.addAll(favouriteList)
            notifyDataSetChanged()
        }
    }

    fun removeItem(position: Int) {
        favouriteList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavouriteItemHolder(FavouritesItemRowBinding.inflate(inflater, parent, false))


    override fun onBindViewHolder(holder: FavouritesRecyclerAdapter.FavouriteItemHolder, position: Int) =
        holder.bindFavourites(favouriteList[position])

    override fun getItemCount() = favouriteList.size

    inner class FavouriteItemHolder(
        private val view: FavouritesItemRowBinding
    ) : RecyclerView.ViewHolder(view.root), View.OnClickListener {

        private lateinit var favourite: Favourite

        init {
            view.deleteButton.setOnClickListener(this)
        }

        fun bindFavourites(favourite: Favourite) {

            view.favourite = favourite
            this.favourite = favourite

            // We use Fresco here because it is supporting caching out of the box
            // so favourite images are available without internet even if app have been restarted
            val uri = Uri.parse(favourite.bit)

            view.favouriteImage.hierarchy.setProgressBarImage(getFrescoProgressBarLoadable())
            setUri(view.favouriteImage, uri, true)

            view.favouriteImage.setOnClickListener {
                setUri(view.favouriteImage, uri, true);
            }
        }

        override fun onClick(p0: View?) {
            listener?.onFavouriteDelete(this.favourite)
        }
    }

    interface OnFavouriteSelectListener {
        fun onFavouriteDelete(favourite: Favourite)
    }

    fun setFavouriteSelectListener(listener: OnFavouriteSelectListener) {
        this.listener = listener
    }
}
