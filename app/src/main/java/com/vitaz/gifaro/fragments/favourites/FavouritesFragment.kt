package com.vitaz.gifaro.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vitaz.gifaro.connectivity.LoadableFragment
import com.vitaz.gifaro.databinding.FavouritesFragmentBinding

class FavouritesFragment: LoadableFragment() {

    private var _binding: FavouritesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
