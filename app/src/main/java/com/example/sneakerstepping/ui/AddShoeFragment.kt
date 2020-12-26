package com.example.sneakerstepping.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sneakerstepping.R
import com.example.sneakerstepping.adapter.AddShoeAdapter
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import kotlinx.android.synthetic.main.fragment_add_shoe.*

class AddShoeFragment : Fragment() {
    private val shoesForUsers = arrayListOf<Shoe>()
    private val addShoeAdapter = AddShoeAdapter(shoesForUsers)
    private val viewModel: SneakerViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAvailableShoes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shoe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        rvAddShoe.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvAddShoe.adapter = addShoeAdapter
        observeShoes()
    }

    private fun observeShoes() {
        viewModel.shoesForUsers.observe(viewLifecycleOwner, {
            shoesForUsers.addAll(it)
            addShoeAdapter.notifyDataSetChanged()
        })
    }

}