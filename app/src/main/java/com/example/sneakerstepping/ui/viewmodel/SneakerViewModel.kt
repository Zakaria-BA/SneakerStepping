package com.example.sneakerstepping.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SneakerViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    val user: LiveData<FirebaseUser?> = firebaseRepository.user

    val registerSucces: LiveData<Boolean> = firebaseRepository.registerSucces

    fun setAuth(auth: FirebaseAuth){
        firebaseRepository.auth = auth
        firebaseRepository.checkForUser()
    }

    fun createUser(user: User, activity: Activity){
        firebaseRepository.createAccount(user, activity)
    }

    fun signIn(user: User, activity: Activity){
        firebaseRepository.signIn(user, activity)
    }

}