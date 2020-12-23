package com.example.sneakerstepping.ui.startup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sneakerstepping.R
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.startup_fragment.*

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
        observeUser()
        createAccountButton.setOnClickListener { navigateUser() }
    }

    private fun navigateUser(){
        navController.navigate(R.id.action_startupFragment_to_registerFragment)
    }

    private fun observeUser(){
        viewModel.user.observe(viewLifecycleOwner, {
            if (it != null){
                navController.navigate(R.id.action_startupFragment_to_homeFragment)
            }
        })
    }


}