package com.vitaz.gifaro.connectivity

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

abstract class LoadableFragment : Fragment() {
    lateinit var loadingProgressBar: ProgressBar
    lateinit var statusButton: Button
    lateinit var connectivityLiveData: ConnectivityLiveData
    lateinit var errorMessage: TextView

    fun onLoadingStateChanged(state: LoadingState, view: View) {
        when (state) {
            LoadingState.LOADING -> {
                loadingProgressBar.visibility = View.VISIBLE
                statusButton.visibility = View.GONE
                errorMessage.visibility = View.GONE
                view.visibility = View.GONE
            }
            LoadingState.LOADED -> {
                view.visibility = View.VISIBLE
                statusButton.visibility = View.GONE
                errorMessage.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
            }
            LoadingState.NO_INTERNET -> {
                view.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                errorMessage.visibility = View.GONE
                statusButton.visibility = View.VISIBLE
            }
            LoadingState.ERROR -> {
                loadingProgressBar.visibility = View.GONE
                view.visibility = View.GONE
                statusButton.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
            }
        }
    }
}
