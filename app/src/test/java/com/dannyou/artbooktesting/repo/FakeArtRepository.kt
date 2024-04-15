package com.dannyou.artbooktesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannyou.artbooktesting.model.ImageResponse
import com.dannyou.artbooktesting.room.Art
import com.dannyou.artbooktesting.util.Resource

class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLivedata = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(), 0, 0))
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLivedata
    }

    private fun refreshData() {
        artsLivedata.postValue(arts)
    }
}