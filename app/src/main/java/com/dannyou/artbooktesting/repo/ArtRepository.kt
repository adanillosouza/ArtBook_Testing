package com.dannyou.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.dannyou.artbooktesting.api.RetrofitAPI
import com.dannyou.artbooktesting.model.ImageResponse
import com.dannyou.artbooktesting.room.Art
import com.dannyou.artbooktesting.room.ArtDao
import com.dannyou.artbooktesting.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val dao: ArtDao,
    private val retrofitAPI: RetrofitAPI
) : ArtRepositoryInterface {

    override suspend fun insertArt(art: Art) {
        dao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        dao.deleteArt(art)
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitAPI.imageSearch(imageString)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }

    override fun getArt(): LiveData<List<Art>> {
        return dao.observeArts()
    }
}