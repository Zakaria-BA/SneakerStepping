package com.example.sneakerstepping.ui.account

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.setAuth(Firebase.auth)
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initViews(){
        registerButton.setOnClickListener { createUser() }
    }

    private fun createUser(){
        if (validateForm()){
            viewModel.createUser(User(etRegisterEmail.text.toString(), etRegisterPassword.text.toString()), requireActivity())
        } else Toast.makeText(requireContext(), "The form isn't correct. Please check your input.", Toast.LENGTH_SHORT).show()
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
}