package com.example.sneakerstepping.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sneakerstepping.R
import com.example.sneakerstepping.adapter.AddShoeAdapter
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_add_shoe.*
import okhttp3.internal.wait
import java.util.*
import kotlin.collections.ArrayList


class AddShoeFragment : Fragment() {
    private val shoesForUsers = arrayListOf<Shoe>()
    private var addShoeAdapter = AddShoeAdapter(shoesForUsers, ::addShoeAndNavigate)
    private val viewModel: SneakerViewModel by activityViewModels()
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        rvAddShoe.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvAddShoe.adapter = addShoeAdapter
        navController = findNavController()
        fab_add_shoe.setOnClickListener { doShoeRequest() }
        observeShoes()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.search_item)
        val btnSearch = searchItem.actionView as SearchView

        btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                search(searchText)
                return true
            }
        })
        super.onPrepareOptionsMenu(menu)
    }

    private fun observeShoes() {
        viewModel.shoesForUsers.observe(viewLifecycleOwner, {
            shoesForUsers.clear()
            shoesForUsers.addAll(it)
            addShoeAdapter.notifyDataSetChanged()
        })
    }

    private fun doShoeRequest(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Which shoe needs to be added to the database?")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Send", { dialogInterface: DialogInterface, i: Int -> sendRequestAndNavigate(input.text.toString())})
        builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun sendRequestAndNavigate(request: String){
        viewModel.sendRequest(request, requireContext())
        navController.navigate(R.id.action_addShoeFragment_to_homeFragment)
    }

    private fun addShoeAndNavigate(shoe: Shoe){
        viewModel.addShoeToCollection(shoe, requireContext())
        navController.navigate(R.id.action_addShoeFragment_to_homeFragment)
    }

    private fun search(searchText: String){
        val filteredShoes = ArrayList<Shoe>()
        val filteredQuery = searchText.trim().toLowerCase(Locale.ROOT)

        for (shoe: Shoe in shoesForUsers){
            val nameSearch = shoe.shoeName.trim().toLowerCase(Locale.ROOT)

            if (nameSearch.contains(filteredQuery)){
                filteredShoes.add(shoe)
            }
            addShoeAdapter = AddShoeAdapter(filteredShoes, ::addShoeAndNavigate)
            rvAddShoe.adapter = addShoeAdapter
        }
    }

}