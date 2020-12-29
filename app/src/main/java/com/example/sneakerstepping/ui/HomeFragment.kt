package com.example.sneakerstepping.ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sneakerstepping.R
import com.example.sneakerstepping.adapter.ShoeAdapter
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class HomeFragment : Fragment(), SensorEventListener {
    private lateinit var navController: NavController
    private val shoes = arrayListOf<Shoe>()
    private val shoeAdapter = ShoeAdapter(shoes, ::putOn)
    private val viewModel: SneakerViewModel by activityViewModels()
    var sensorManager: SensorManager? = null
    private var hasShoesOnFoot: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCollectionOfShoes(requireContext())
        initViews()
    }

    override fun onResume() {
        super.onResume()

        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null){
            Toast.makeText(requireContext(), "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun initViews(){
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        navController = findNavController()
        rvCollectionShoes.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCollectionShoes.adapter = shoeAdapter
        removeShoeButton.setOnClickListener { removeShoe() }
        observeShoeOnfeet()
        observeShoes()
        fab.setOnClickListener { navigateUserToAddShoe() }
    }

    private fun navigateUserToAddShoe(){
        navController.navigate(R.id.action_homeFragment_to_addShoeFragment)
    }

    private fun observeShoes() {
        viewModel.collectionOfShoes.observe(viewLifecycleOwner, {
            shoes.clear()
            shoes.addAll(it)
            shoeAdapter.notifyDataSetChanged()
        })
    }

    private fun observeShoeOnfeet(){
        viewModel.shoeOnFoot.observe(viewLifecycleOwner, {
            if (it !== null) {
                updateUi(it)
                hasShoesOnFoot = true
            }
        })
    }

    private fun putOn(shoe: Shoe){
        viewModel.setPutOnShoe(shoe)
    }

    private fun removeShoe(){
        viewModel.removeShoe()
        updateUi(null)
        hasShoesOnFoot = false
    }


    private fun updateUi(shoe: Shoe?) {
        if (shoe == null){
            tvWearing.text = "You have no shoes on foot. :("
            shoeOnFootCard.isVisible = false
        } else {
            tvWearing.text = "Wearing: " + shoe?.shoeName
            shoeOnFootCard.isVisible = true
            tvShoeOnFootTitle.text = shoe?.shoeName
            Glide.with(requireContext()).load(shoe?.shoeImage).into(ivShoeOnFootPicture)
            tvShoeMilageEver.text = "Total milage covered: " + shoe?.milageCovered.toString()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        var currentMilage = viewModel.shoeOnFoot.value!!.milageCovered
        if (hasShoesOnFoot){
            if (p0 != null) {
                currentMilage!!.plus(p0.values[0].toLong())
                tvShoeMilageEver.setText(p0.values[0].toString())
                Handler().postDelayed({
                    viewModel.updateShoe(currentMilage, requireContext())
                }, 1000)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}