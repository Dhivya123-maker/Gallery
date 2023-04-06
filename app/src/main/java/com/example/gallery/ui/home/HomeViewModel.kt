package com.example.gallery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gallery.api.PhotosApi
import com.example.gallery.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: PhotosApi,
) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)) {
        PagingSource(apiService)

    }.flow.cachedIn(viewModelScope)

    }


