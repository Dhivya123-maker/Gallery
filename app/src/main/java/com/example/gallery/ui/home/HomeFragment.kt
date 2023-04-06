package com.example.gallery.ui.home

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gallery.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private  lateinit var _binding: FragmentHomeBinding
    private var homeRecyclerAdapter = HomeRecyclerAdapter()
    private var isFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0D4F8B")))


        internet()


        _binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                _binding.refreshLayout.setRefreshing(false)
                 internet()
            }
        )

        return _binding.root
    }

    fun internet(){
        if (checkForInternet(requireContext())) {
            data()
        }else{
            message(requireActivity())
        }
    }

    @SuppressLint("RestrictedApi")
    fun data(){

            _binding.rvPhotos.adapter = homeRecyclerAdapter


        homeRecyclerAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                if (isFirstTime) {
                    _binding.rvPhotos
                } else {
                    _binding.progressBar.isVisible = true

                }
            } else {
                if (isFirstTime) {
                    _binding.rvPhotos.postDelayed({
                        _binding.rvPhotos
                        isFirstTime = false
                    }, 2000)
                }
                _binding.progressBar.isVisible = false
                val marginLayoutParams = MarginLayoutParams(_binding.rvPhotos.getLayoutParams())
                marginLayoutParams.setMargins(0, 10, 0, 10)
                _binding.rvPhotos.setLayoutParams(marginLayoutParams)

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }

        lifecycleScope.launch {
            homeViewModel.listData.collect {
                homeRecyclerAdapter.submitData(it)

            }

        }


    }



}




private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    fun message(activity: Activity) {
        val rootView: View = activity.getWindow().getDecorView().findViewById(R.id.content)
        Snackbar.make(rootView, "check your internet connection and retry", Snackbar.LENGTH_LONG).show()
    }


