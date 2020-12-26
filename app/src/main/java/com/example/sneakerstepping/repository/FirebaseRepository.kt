package com.example.sneakerstepping.repository

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.models.User
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class FirebaseRepository {
    lateinit var auth: FirebaseAuth
    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val _registerSucces: MutableLiveData<Boolean> = MutableLiveData()
    private val _shoesForUser: MutableLiveData<ArrayList<Shoe>> = MutableLiveData()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var avaialbleShoes = firestore.collection("schoenen")

    val registerSucces: LiveData<Boolean>
        get() = _registerSucces

    val user: LiveData<FirebaseUser?>
        get() = _user

    val shoesForUser: LiveData<ArrayList<Shoe>>
        get() = _shoesForUser

    fun checkForUser() {
        _user.value = auth.currentUser
    }

    private val _retrieveShoesSucces: MutableLiveData<Boolean> = MutableLiveData()

    val retrievShoesSucces: LiveData<Boolean>
        get() = _retrieveShoesSucces

    fun createAccount(user: User, activity: Activity) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        _user.value = auth.currentUser
                        _registerSucces.value = true
                    } else {
                        _registerSucces.value = false
                        Log.w(TAG, it.exception)
                        Toast.makeText(activity, "Signup failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun signIn(user: User, activity: Activity) {
        auth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        _user.value = auth.currentUser
                    } else {
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }

    suspend fun getAvailableShoes() {
        var availableShoesForUser: ArrayList<Shoe> = ArrayList()
        try {
            withTimeout(5_000) {
                val data = avaialbleShoes
                        .get()
                        .await()

                for (currentItem: DocumentSnapshot in data.documents) {
                    val shoe = Shoe(currentItem.id, currentItem.getString("name").toString(), currentItem.getString("image").toString(), currentItem.getString("type").toString(), null)
                    availableShoesForUser.add(shoe)
                }
                _shoesForUser.value = availableShoesForUser
                _retrieveShoesSucces.value = true
            }
        } catch (e: Exception) {
            _retrieveShoesSucces.value = false
            throw e
        }
    }
}