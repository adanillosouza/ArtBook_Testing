package com.dannyou.artbooktesting.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dannyou.artbooktesting.R
import com.dannyou.artbooktesting.adapter.ArtRecyclerAdapter
import com.dannyou.artbooktesting.databinding.FragmentArtsBinding
import com.dannyou.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    private val artRecyclerAdapter: ArtRecyclerAdapter
) : Fragment(R.layout.fragment_arts) {

    private var binding: FragmentArtsBinding? = null
    private lateinit var viewModel: ArtViewModel
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return  true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArtsBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        setupFloatButton()
        setupRecyclerView()
        observers()
    }

    private fun setupRecyclerView() {
        binding?.recyclerViewArt?.apply {
            adapter = artRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(swipeCallBack).attachToRecyclerView(this)
        }
    }

    private fun observers() {
        viewModel.artList.observe(viewLifecycleOwner) {
            artRecyclerAdapter.arts = it
        }
    }

    private fun setupFloatButton() {
        binding?.floatingActionButton?.setOnClickListener {
            findNavController().navigate(R.id.artDetailsFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}