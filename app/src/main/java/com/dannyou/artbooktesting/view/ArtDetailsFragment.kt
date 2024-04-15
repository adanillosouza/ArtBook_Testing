package com.dannyou.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.dannyou.artbooktesting.R
import com.dannyou.artbooktesting.databinding.FragmentArtDetailsBinding
import com.dannyou.artbooktesting.util.Status
import com.dannyou.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private var binding: FragmentArtDetailsBinding? = null
    private lateinit var viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArtDetailsBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        setupButtons()
        observers()
    }

    private fun setupButtons() {
        binding?.apply {
            button.setOnClickListener {
                viewModel.makeArt(
                    name = editTextArtName.text.toString(),
                    artistName = editTextArtistName.text.toString(),
                    year = editTextYear.text.toString()
                )
            }

            ivArtImageView.setOnClickListener {
                findNavController().navigate(R.id.imageApiFragment)
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun observers() {
        viewModel.run {
            selectedImageUrl.observe(viewLifecycleOwner) { url ->
                binding?.apply {
                    glide.load(url).into(ivArtImageView)
                }
            }
            insertArtMessage.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                        viewModel.resetInsertArtMsg()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message?: "Error", Toast.LENGTH_LONG).show()
                        viewModel.resetInsertArtMsg()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}