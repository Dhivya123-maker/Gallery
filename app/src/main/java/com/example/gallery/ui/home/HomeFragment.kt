package com.example.gallery.ui.home

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gallery.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var _binding: FragmentHomeBinding
   private var homeRecyclerAdapter = HomeRecyclerAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0D4F8B")))


        _binding.progressBarSearch.visibility = View.VISIBLE

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

    fun data(){
        _binding.progressBarSearch.visibility = View.GONE
        _binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext())
        _binding.rvPhotos.adapter = homeRecyclerAdapter
        _binding.rvPhotos.setHasFixedSize(false)

        homeViewModel.photos.observe(viewLifecycleOwner) {
            homeRecyclerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
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
            // if the android version is below M
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


