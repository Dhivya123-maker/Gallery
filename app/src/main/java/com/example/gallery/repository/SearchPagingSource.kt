package com.example.gallery.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gallery.api.PhotosApi
import com.example.gallery.models.Photo
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val photosApi: PhotosApi,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = photosApi.searchImage(query = query, page = position, perPage = params.loadSize)
            val photos = response.photos?.photo

            LoadResult.Page(
                data = photos!!,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

        override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
            return null
        }
    }


