package com.vitaz.gifaro.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
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
import com.vitaz.gifaro.misc.Constants
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
        bindSearch()

        return view
    }

    private fun bindSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    onLoadingStateChanged(LoadingState.LOADING, gifListRecyclerView)
                    gifListRecyclerView.scrollToPosition(0)
                    gifsViewModel.currentPage = 0
                    gifsViewModel.getNewData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                gifsViewModel.searchText.postValue(newText)
                return false
            }
        })
        binding.search.setOnCloseListener {
            gifsViewModel.currentPage = 0
            gifsViewModel.searchText.postValue("")
            onLoadingStateChanged(LoadingState.LOADING, gifListRecyclerView)
            gifListRecyclerView.scrollToPosition(0)
            gifsViewModel.getNewData()
            false
        }
        binding.search.setOnClickListener(View.OnClickListener {
            binding.search.isIconified = false
        })
    }

    private fun setupGifListRecyclerAdapter() {
        gifListRecyclerAdapter = GifListRecyclerAdapter(requireContext())
        gifListRecyclerView.adapter = gifListRecyclerAdapter
        val layout = LinearLayoutManager(requireContext())
        gifListRecyclerAdapter.setGifSelectListener(this)
        gifListRecyclerView.layoutManager = layout

        // This requires as a workaround for item dynamical resizing when click favourite button, caused by Fresco
        gifListRecyclerView.itemAnimator = null


        gifListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    gifsViewModel.getNewData()
                    Toast.makeText(activity, "Loading page ${gifsViewModel.currentPage} of GIFs. ${Constants.REQUEST_LIMIT * gifsViewModel.currentPage} total.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun bindObservers() {

        gifsViewModel.favouriteList.observe(viewLifecycleOwner, Observer {
            gifListRecyclerAdapter.setFavourites(it)
        })

        gifsViewModel.gifList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                onLoadingStateChanged(LoadingState.ERROR, gifListRecyclerView)
            } else {
                gifListRecyclerAdapter.setGifs(it)
                onLoadingStateChanged(LoadingState.LOADED, gifListRecyclerView)
            }
        })

        connectivityLiveData.observe(viewLifecycleOwner, { isAvailable ->
            when (isAvailable) {
                true -> {
                    gifsViewModel.getNewData()
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
            gifsViewModel.deleteFromFavourite(gif.id)
        } else {
            gifsViewModel.addNewFavourite(gif)
        }
    }

}
