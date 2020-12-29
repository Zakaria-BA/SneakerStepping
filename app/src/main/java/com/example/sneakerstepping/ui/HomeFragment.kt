package com.example.sneakerstepping.ui

import android.content.ContentValues.TAG
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sneakerstepping.R
import com.example.sneakerstepping.adapter.ShoeAdapter
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.fixedRateTimer

class HomeFragment : Fragment(), SensorEventListener {
    private lateinit var navController: NavController
    private val shoes = arrayListOf<Shoe>()
    private val shoeAdapter = ShoeAdapter(shoes, ::putOn)
    private val viewModel: SneakerViewModel by activityViewModels()
    var sensorManager: SensorManager? = null
    private var numSteps: Long = 0
    private var hasShoesOnFoot: Boolean = false
    private var totalSteps: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager?
        viewModel.getCollectionOfShoes(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun initViews() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        navController = findNavController()
        rvCollectionShoes.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCollectionShoes.adapter = shoeAdapter
        removeShoeButton.setOnClickListener { removeShoe() }
        observeShoeOnfeet()
        observeShoes()
        shoeAdapter.notifyDataSetChanged()
        fab.setOnClickListener { navigateUserToAddShoe() }
        createItemTouchHelper().attachToRecyclerView(rvCollectionShoes)
    }

    private fun navigateUserToAddShoe() {
        navController.navigate(R.id.action_homeFragment_to_addShoeFragment)
    }

    private fun observeShoes() {
        viewModel.collectionOfShoes.observe(viewLifecycleOwner, {
            shoes.clear()
            shoes.addAll(it)
            shoeAdapter.notifyDataSetChanged()
        })
    }

    private fun observeShoeOnfeet() {
        viewModel.shoeOnFoot.observe(viewLifecycleOwner, {
            if (it !== null) {
                updateUi(it)
                hasShoesOnFoot = true
            }
        })
    }

    private fun putOn(shoe: Shoe) {
        totalSteps = 0
        numSteps = 0
        viewModel.setPutOnShoe(shoe, requireContext())
    }

    private fun removeShoe() {
        if (totalSteps != 0.toLong()){
            var shoe = Shoe(viewModel.shoeOnFoot.value!!.shoeId, viewModel.shoeOnFoot.value!!.shoeName, viewModel.shoeOnFoot.value!!.shoeImage, viewModel.shoeOnFoot.value!!.shoeType,
                    totalSteps)
            viewModel.updateShoe(shoe, requireContext())
        }
        viewModel.removeShoe()
        viewModel.getCollectionOfShoes(requireContext())
        updateUi(null)
        hasShoesOnFoot = false
        totalSteps = 0
    }


    private fun updateUi(shoe: Shoe?) {
        if (shoe == null) {
            tvWearing.text = "You have no shoes on foot. :("
            shoeOnFootCard.isVisible = false
        } else {
            tvWearing.text = "Wearing: " + shoe?.shoeName
            shoeOnFootCard.isVisible = true
            tvShoeOnFootTitle.text = shoe?.shoeName
            Glide.with(requireContext()).load(shoe?.shoeImage).into(ivShoeOnFootPicture)
            tvShoeMilageEver.text = "Total steps taken: " + shoe?.milageCovered.toString()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (hasShoesOnFoot) {
            numSteps++
            totalSteps = viewModel.shoeOnFoot.value?.milageCovered?.plus(numSteps)!!
            tvShoeMilageEver.text = "Total steps taken: " + totalSteps
            Handler().postDelayed({
                if (hasShoesOnFoot) {
                    var shoe = Shoe(viewModel.shoeOnFoot.value!!.shoeId, viewModel.shoeOnFoot.value!!.shoeName, viewModel.shoeOnFoot.value!!.shoeImage, viewModel.shoeOnFoot.value!!.shoeType,
                            totalSteps)
                    viewModel.updateShoe(shoe, requireContext())
                }
            }, 30000)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (hasShoesOnFoot){
                    if (shoes[position].shoeId == viewModel.shoeOnFoot.value?.shoeId){
                        removeShoe()
                    }
                }
                viewModel.deleteShoeFromCollection(shoes[position], requireContext(), position)
                shoes.removeAt(position)

                shoeAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }


}