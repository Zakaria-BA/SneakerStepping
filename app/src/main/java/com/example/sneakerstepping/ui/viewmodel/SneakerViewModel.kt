package com.example.sneakerstepping.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.lang.Exception

class SneakerViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    val user: LiveData<FirebaseUser?> = firebaseRepository.user
    val shoesForUsers: LiveData<ArrayList<Shoe>> = firebaseRepository.shoesForUser

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

    fun signOut() {
        firebaseRepository.signOut()
    }

    fun getAvailableShoes(){
        viewModelScope.launch {
            try {
                firebaseRepository.getAvailableShoes()
            } catch (e: Exception){
                throw e
            }
        }
    }

}