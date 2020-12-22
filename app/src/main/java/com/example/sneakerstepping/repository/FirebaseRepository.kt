package com.example.sneakerstepping.repository

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sneakerstepping.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseRepository {
    lateinit var auth: FirebaseAuth
    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()

    val user: LiveData<FirebaseUser?>
        get() = _user

    fun createAccount(user: User, activity: Activity){
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(activity){
                if (it.isSuccessful){
                    _user.value = auth.currentUser
                } else {
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}