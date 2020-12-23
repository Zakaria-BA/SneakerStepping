package com.example.sneakerstepping.repository

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sneakerstepping.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseRepository {
    lateinit var auth: FirebaseAuth
    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val _registerSucces: MutableLiveData<Boolean> = MutableLiveData()

    val registerSucces: LiveData<Boolean>
        get() = _registerSucces

    val user: LiveData<FirebaseUser?>
        get() = _user

    fun checkForUser(){
        _user.value = auth.currentUser
    }

    fun createAccount(user: User, activity: Activity){
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(activity){
                if (it.isSuccessful){
                    _user.value = auth.currentUser
                    _registerSucces.value = true
                } else {
                    _registerSucces.value = false
                    Log.w(TAG, it.exception)
                    Toast.makeText(activity, "Signup failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signIn(user: User, activity: Activity){
        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(activity){
                if(it.isSuccessful){
                    _user.value = auth.currentUser
                } else{
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signOut(){
        auth.signOut()
    }
}