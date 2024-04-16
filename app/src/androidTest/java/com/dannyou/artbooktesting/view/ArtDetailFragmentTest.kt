package com.dannyou.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.dannyou.artbooktesting.R
import com.dannyou.artbooktesting.getOrAwaitValue
import com.dannyou.artbooktesting.launchFragmentInHiltContainer
import com.dannyou.artbooktesting.repo.FakeArtRepository
import com.dannyou.artbooktesting.room.Art
import com.dannyou.artbooktesting.viewmodel.ArtViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromArtDetailFragmentTimageApi() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.imageApiFragment))
        Mockito.verify(navController).navigate(R.id.imageApiFragment)
    }

    @Test
    fun testOnBackPressed() {
        val navController = mockk<NavController>()

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave() {

        val testViewModel = ArtViewModel(FakeArtRepository())
        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory) {
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.editTextArtName))
            .perform(ViewActions.replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.editTextArtistName))
            .perform(ViewActions.replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.editTextYear))
            .perform(ViewActions.replaceText("1500"))
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click())

        testViewModel.artList.getOrAwaitValue()
        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art(
                name = "Mona Lisa",
                artist = "Da Vinci",
                year = 1500,
                imageUrl = null
            )
        )
    }
}