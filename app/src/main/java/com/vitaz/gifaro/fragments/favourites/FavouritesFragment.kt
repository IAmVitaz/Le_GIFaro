package com.vitaz.gifaro.fragments.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vitaz.gifaro.MainApplication
import com.vitaz.gifaro.connectivity.ConnectivityLiveData
import com.vitaz.gifaro.connectivity.LoadableFragment
import com.vitaz.gifaro.connectivity.LoadingState
import com.vitaz.gifaro.databinding.FavouritesFragmentBinding
import com.vitaz.gifaro.fragments.GifsViewModel
import com.vitaz.gifaro.fragments.list.GifListRecyclerAdapter

class FavouritesFragment: LoadableFragment() {

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

        // Fetch data from ROOM if no favourites presented in the list:
        if (gifsViewModel.favouriteList.value.isNullOrEmpty()) {
            gifsViewModel.getFavouriteList()
        }

//        setupFavouriteListRecyclerAdapter()
        bindObservers()

        return view
    }

//    fun setupFavouriteListRecyclerAdapter() {
//        gifListRecyclerAdapter = GifListRecyclerAdapter(requireContext())
//        gifListRecyclerView.adapter = gifListRecyclerAdapter
//        val layout = LinearLayoutManager(requireContext())
//        gifListRecyclerAdapter.setGifSelectListener(this)
//        gifListRecyclerView.layoutManager = layout
//    }

    fun bindObservers() {
        gifsViewModel.favouriteList.observe(viewLifecycleOwner, Observer {

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
