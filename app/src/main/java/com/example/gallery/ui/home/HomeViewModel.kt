package com.example.gallery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.gallery.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val photosRepository: PhotosRepository
) : ViewModel() {

    val photos = photosRepository.getImageResults().cachedIn(viewModelScope)


}
