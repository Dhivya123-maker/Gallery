package com.example.gallery.ui.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var _binding: FragmentSearchBinding

    private var searchPhotoAdapter = SearchPhotosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        _binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if(query != null){
                    _binding.rvSearch.scrollToPosition(0)
                    searchViewModel.searchPhotos(query)
                    _binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        _binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        _binding.rvSearch.adapter = searchPhotoAdapter

        searchViewModel.photos.observe(viewLifecycleOwner) {
            searchPhotoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


        return _binding.root

    }


}


