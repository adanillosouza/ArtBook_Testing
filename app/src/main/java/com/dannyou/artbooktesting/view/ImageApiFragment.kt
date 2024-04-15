package com.dannyou.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dannyou.artbooktesting.R
import com.dannyou.artbooktesting.adapter.ImageRecyclerAdapter
import com.dannyou.artbooktesting.databinding.FragmentImageApiBinding
import com.dannyou.artbooktesting.util.Status
import com.dannyou.artbooktesting.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter
) : Fragment(R.layout.fragment_image_api) {

    private lateinit var viewModel: ArtViewModel
    private var binding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        binding = FragmentImageApiBinding.bind(view)
        observers()

        var job: Job? = null
        binding?.editTextSearch?.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForimkage(it.toString())
                    }
                }
            }
        }

        binding?.imageRecyclerView?.adapter = imageRecyclerAdapter
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectImage(it)
        }
    }

    private fun observers() {
        viewModel.run {
            imageList.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val urls = it.data?.hits?.map { imageResult ->
                            imageResult.previewURL
                        }
                        imageRecyclerAdapter.images = urls ?: listOf()
                        binding?.progressBar?.visibility = GONE
                    }

                    Status.ERROR -> {
                        binding?.progressBar?.visibility = GONE
                        Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG)
                            .show()
                    }

                    Status.LOADING -> {
                        binding?.progressBar?.visibility = VISIBLE
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