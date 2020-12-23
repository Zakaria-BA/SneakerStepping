package com.example.sneakerstepping.ui.account

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sneakerstepping.R
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.register_fragment.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {
    private val viewModel: SneakerViewModel by activityViewModels()
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initViews()
    }

    private fun initViews() {
        registerButton.setOnClickListener { createUser() }
        observeRegisteringSucces()
    }

    private fun updateUi(registering: Boolean) {
        if (registering) {
            clRegister.isVisible = false
            pb_loading_register.isVisible = true
        } else {
            clRegister.isVisible = true
            pb_loading_register.isVisible = false
        }
    }

    private fun createUser() {
        if (validateForm()) {
            updateUi(true)
            viewModel.createUser(
                User(
                    etRegisterEmail.text.toString(),
                    etRegisterPassword.text.toString()
                ), requireActivity()
            )
        } else {
            Toast.makeText(
                requireContext(),
                "The form isn't correct. Please check your input.",
                Toast.LENGTH_SHORT
            ).show()
            updateUi(false)
        }
    }

    private fun validateForm(): Boolean {
        var isFormValid = false
        if (etRegisterEmail.text.isNullOrEmpty() || etRegisterPassword.text.isNullOrEmpty() || etRegisterPassword.text.isNullOrEmpty()) {
            isFormValid = false
        } else if (Patterns.EMAIL_ADDRESS.matcher(etRegisterEmail.text).matches()) {
            isFormValid = etRegisterPassword.text!!.length > 5
        }
        return isFormValid
    }

    private fun observeRegisteringSucces(){
        viewModel.registerSucces.observe(viewLifecycleOwner, {
            if (it){
                navController.navigate(R.id.action_registerFragment_to_homeFragment)
            } else {
                updateUi(false)
            }
        })
    }
}