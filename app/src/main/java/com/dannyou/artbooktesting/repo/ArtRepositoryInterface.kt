package com.dannyou.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.dannyou.artbooktesting.model.ImageResponse
import com.dannyou.artbooktesting.room.Art
import com.dannyou.artbooktesting.util.Resource

interface ArtRepositoryInterface {
    suspend fun insertArt(art: Art)
    suspend fun deleteArt(art: Art)
    suspend fun searchImage(imageString: String): Resource<ImageResponse>
    fun getArt(): LiveData<List<Art>>
}