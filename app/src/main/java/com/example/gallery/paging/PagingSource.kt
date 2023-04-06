package com.example.gallery.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.example.gallery.api.PhotosApi
import com.example.gallery.models.Photo

class PagingSource(private val apiService: PhotosApi) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getImages(page = currentPage, perPage = params.loadSize)
            val responseData = mutableListOf<Photo>()
            val data = response.photos?.photo
            responseData.addAll(data!!)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }

        catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}
//private const val STARTING_PAGE_INDEX = 1
//
//class RickyMortyPagingSource
//    (
//    private val apiService: PhotosApi,
//) : PagingSource<Int, Photo>() {
//
//    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
//        return null
//    }
//
//    override suspend fun load(params: LoadParams<Int>):
//            LoadResult<Int, Photo> {
//        val position = params.key ?: STARTING_PAGE_INDEX
//
//
//        return try {
//            val response = apiService.getImages(page = position, perPage = params.loadSize )
//            val photos = response.photos?.photo
//
//            LoadResult.Page(
//                data = photos!!,
//                prevKey = if(position ==STARTING_PAGE_INDEX) null else position - 1,
//                nextKey = if(photos.isEmpty()) null else position + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//
//    }
//}