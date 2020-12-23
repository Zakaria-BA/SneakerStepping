package com.example.sneakerstepping.ui.startup

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sneakerstepping.R
import com.example.sneakerstepping.`interface`.DrawerLocker
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.startup_fragment.*
import java.lang.Exception
import kotlin.math.log


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartupFragment : Fragment() {
    lateinit var navController: NavController
    private val viewModel: SneakerViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.setAuth(Firebase.auth)
        return inflater.inflate(R.layout.startup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        initViews()
    }

    private fun initViews() {
        observeUser()
        createAccountButton.setOnClickListener { navigateUser() }
        logInButton.setOnClickListener { logIn() }
    }

    private fun logIn() {
        viewModel.signIn(
            User(etEmail.text.toString(), etPassword.text.toString()),
            requireActivity()
        )
        updateUi(true)
    }

    private fun updateUi(logingIn: Boolean) {
        if (logingIn) {
            clLogin.isVisible = false
            pb_loading_login.isVisible = true
        } else {
            clLogin.isVisible = true
            pb_loading_login.isVisible = false
        }
    }



    private fun navigateUser() {
        navController.navigate(R.id.action_startupFragment_to_registerFragment)
    }

    private fun observeUser(){
        viewModel.user.observe(viewLifecycleOwner, {
            if (it != null) {
                try {
                    navController.navigate(R.id.action_startupFragment_to_homeFragment)
                } catch (e: Exception){
                    Log.e(TAG, e.toString())
                }
            } else updateUi(false)
        })
    }


}