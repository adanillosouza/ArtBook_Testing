package com.dannyou.artbooktesting.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.dannyou.artbooktesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ArtDatabase
    private lateinit var dao: ArtDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArtDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.artDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertArtTesting() = runTest {

        val exampleArt = Art(
            name = "Mona Lisa",
            artist = "Da Vinci",
            year = 1970,
            imageUrl = "teste.com"
        )

        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat()

    }

    @Test
    fun deleteArtTesting() {

    }
}