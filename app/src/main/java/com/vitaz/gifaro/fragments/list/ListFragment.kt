package com.vitaz.gifaro.fragments.list

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
import com.vitaz.gifaro.databinding.ListFragmentBinding
import com.vitaz.gifaro.fragments.GifsViewModel
import com.vitaz.gifaro.networking.NetworkChecker
import com.vitaz.gifaro.networking.dto.GifObject

class ListFragment : LoadableFragment(), GifListRecyclerAdapter.OnGifSelectListener {

    private lateinit var gifListRecyclerAdapter: GifListRecyclerAdapter
    private lateinit var gifListRecyclerView: RecyclerView


    private val gifsViewModel: GifsViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    private var _binding: ListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        gifListRecyclerView = binding.gifListRecyclerView
        connectivityLiveData = ConnectivityLiveData(MainApplication.instance)
        loadingProgressBar = binding.loadingProgressBar
        statusButton = binding.statusButton
        errorMessage = binding.errorMessage

        setupGifListRecyclerAdapter()
        bindObservers()

        return view
    }

    private fun setupGifListRecyclerAdapter() {
        gifListRecyclerAdapter = GifListRecyclerAdapter(requireContext())
        gifListRecyclerView.adapter = gifListRecyclerAdapter
        val layout = LinearLayoutManager(requireContext())
        gifListRecyclerAdapter.setGifSelectListener(this)
        gifListRecyclerView.layoutManager = layout
    }

    private fun bindObservers() {

        gifsViewModel.favouriteList.observe(viewLifecycleOwner, Observer {
            gifListRecyclerAdapter.setFavourites(it)
        })

        gifsViewModel.gifList.observe(viewLifecycleOwner, Observer {
            gifListRecyclerAdapter.setGifs(it)
            onLoadingStateChanged(LoadingState.LOADED, gifListRecyclerView)
        })

        connectivityLiveData.observe(viewLifecycleOwner, { isAvailable ->
            when (isAvailable) {
                true -> {
//                    if (gifsViewModel.gifList.value.isNullOrEmpty()) {
//                        gifsViewModel.getTrending()
//                        onLoadingStateChanged(LoadingState.LOADING, gifListRecyclerView)
//                    } else onLoadingStateChanged(LoadingState.LOADED, gifListRecyclerView)
                    gifsViewModel.getTrending()
                    onLoadingStateChanged(LoadingState.LOADING, gifListRecyclerView)

                }
                false -> onLoadingStateChanged(LoadingState.NO_INTERNET, gifListRecyclerView)
            }
        })
        if (!NetworkChecker.isNetworkAvailable(requireContext())) {
            onLoadingStateChanged(LoadingState.NO_INTERNET, gifListRecyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGifSelect(gif: GifObject) {
        val favouriteList = gifListRecyclerAdapter.getListOfFavourites()
        if (gif.id in favouriteList) {
            gifsViewModel.deleteFromFavourite(gif)
        } else {
            gifsViewModel.addNewFavourite(gif)
        }
        Log.d("ADDED TO ROOM", "Added to room: ${gif.title}")
    }

}
