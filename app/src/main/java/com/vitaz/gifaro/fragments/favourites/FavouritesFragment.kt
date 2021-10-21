package com.vitaz.gifaro.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vitaz.gifaro.MainApplication
import com.vitaz.gifaro.connectivity.ConnectivityLiveData
import com.vitaz.gifaro.connectivity.LoadableFragment
import com.vitaz.gifaro.connectivity.LoadingState
import com.vitaz.gifaro.database.tables.favourite.Favourite
import com.vitaz.gifaro.databinding.FavouritesFragmentBinding
import com.vitaz.gifaro.fragments.GifsViewModel

class FavouritesFragment: LoadableFragment(), FavouritesRecyclerAdapter.OnFavouriteSelectListener {

    private lateinit var favouriteListRecyclerAdapter: FavouritesRecyclerAdapter
    private lateinit var favouriteListRecyclerView: RecyclerView

    private val gifsViewModel: GifsViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    private var _binding: FavouritesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        favouriteListRecyclerView = binding.favouriteListRecyclerView
        connectivityLiveData = ConnectivityLiveData(MainApplication.instance)
        loadingProgressBar = binding.loadingProgressBar
        statusButton = binding.statusButton
        errorMessage = binding.errorMessage

        // Fetch data from ROOM if no favourites presented in the list:
        if (gifsViewModel.favouriteList.value.isNullOrEmpty()) {
            gifsViewModel.getFavouriteList()
        }

        setupFavouriteListRecyclerAdapter()
        bindObservers()

        return view
    }

    private fun setupFavouriteListRecyclerAdapter() {
        favouriteListRecyclerAdapter = FavouritesRecyclerAdapter(requireContext())
        favouriteListRecyclerView.adapter = favouriteListRecyclerAdapter
        val layout = GridLayoutManager(requireContext(), 2)
        favouriteListRecyclerAdapter.setFavouriteSelectListener(this)
        favouriteListRecyclerView.layoutManager = layout
    }

    private fun bindObservers() {
        gifsViewModel.favouriteList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                onLoadingStateChanged(LoadingState.ERROR, favouriteListRecyclerView)
            } else {
                favouriteListRecyclerAdapter.setFavourites(it)
                onLoadingStateChanged(LoadingState.LOADED, favouriteListRecyclerView)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavouriteDelete(favourite: Favourite) {
        gifsViewModel.deleteFromFavourite(favourite.id)
    }

}
