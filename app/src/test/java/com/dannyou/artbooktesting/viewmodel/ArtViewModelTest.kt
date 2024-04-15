package com.dannyou.artbooktesting.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dannyou.artbooktesting.getOrAwaitValueTest
import com.dannyou.artbooktesting.repo.FakeArtRepository
import com.dannyou.artbooktesting.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {
        //Test Doubles
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun insertArtWithoutYearReturnsError() {
        viewModel.makeArt(
            name = "Mona Lisa",
            artistName = "Da Vinci",
            year = ""
        )
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`() {
        viewModel.makeArt(
            name = "",
            artistName = "Da Vinci",
            year = "1970"
        )
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artist returns error`() {
        viewModel.makeArt(
            name = "Mona Lisa",
            artistName = "",
            year = "1970"
        )
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

}