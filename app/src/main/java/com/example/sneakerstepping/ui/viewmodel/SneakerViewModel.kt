package com.example.sneakerstepping.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.models.User
import com.example.sneakerstepping.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SneakerViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    val user: LiveData<FirebaseUser?> = firebaseRepository.user
    val loginSucces: LiveData<Boolean> = firebaseRepository.loginSucces
    val shoesForUsers: LiveData<ArrayList<Shoe>> = firebaseRepository.shoesForUser
    val registerSucces: LiveData<Boolean> = firebaseRepository.registerSucces
    val collectionOfShoes: LiveData<ArrayList<Shoe>> = firebaseRepository.collectionOfShoes

    private val _shoeOnFoot: MutableLiveData<Shoe> = MutableLiveData()

    val shoeOnFoot: LiveData<Shoe>
        get() = _shoeOnFoot


    fun setAuth(auth: FirebaseAuth) {
        firebaseRepository.auth = auth
        firebaseRepository.checkForUser()
    }

    fun createUser(user: User, activity: Activity) {
        firebaseRepository.createAccount(user, activity)
    }

    fun signIn(user: User, activity: Activity) {
        firebaseRepository.signIn(user, activity)
    }

    fun signOut() {
        firebaseRepository.signOut()
    }

    fun sendRequest(request: String, context: Context) {
        firebaseRepository.addRequestToDatabase(request, context)
    }

    fun getAvailableShoes() {
        viewModelScope.launch {
            try {
                firebaseRepository.getAvailableShoes()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun addShoeToCollection(shoe: Shoe, context: Context) {
        firebaseRepository.addShoeToCollection(shoe, context)
    }

    fun getCollectionOfShoes(context: Context) {
        viewModelScope.launch {
            try {
                firebaseRepository.getCollectionOfShoes(context)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun setPutOnShoe(shoe: Shoe, context: Context) {
        if (_shoeOnFoot.value?.shoeId.isNullOrEmpty()) {
            _shoeOnFoot.value = shoe
        } else if (shoe.shoeId == _shoeOnFoot.value?.shoeId) {
            Toast.makeText(context, "Already on foot my boi!", Toast.LENGTH_SHORT).show()
        } else _shoeOnFoot.value = shoe
    }

    fun removeShoe() {
        _shoeOnFoot.value = null
    }

    fun updateShoe(milage: Long, context: Context) {
        firebaseRepository.updateShoeMilage(Shoe(shoeOnFoot.value!!.shoeId, shoeOnFoot.value!!.shoeName, shoeOnFoot.value!!.shoeImage, shoeOnFoot.value!!.shoeType, milage), context)
    }

}