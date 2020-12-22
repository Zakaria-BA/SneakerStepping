package com.example.sneakerstepping.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SneakerViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    val user: LiveData<FirebaseUser?> = firebaseRepository.user

    fun setAuth(auth: FirebaseAuth){
        firebaseRepository.auth = auth
    }

    fun createUser(user: User, activity: Activity){
        firebaseRepository.createAccount(user, activity)
    }

}