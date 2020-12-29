package com.example.sneakerstepping.repository

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.provider.Settings.Secure.ANDROID_ID
import android.provider.Settings.Secure.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sneakerstepping.models.Shoe
import com.example.sneakerstepping.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.AccessController.getContext


class FirebaseRepository {
    lateinit var auth: FirebaseAuth
    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val _registerSucces: MutableLiveData<Boolean> = MutableLiveData()
    private val _loginSucces: MutableLiveData<Boolean> = MutableLiveData()
    private val _collectionOfShoes: MutableLiveData<ArrayList<Shoe>> = MutableLiveData()
    private val _shoesForUser: MutableLiveData<ArrayList<Shoe>> = MutableLiveData()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val loginSucces: LiveData<Boolean>
        get() = _loginSucces

    val collectionOfShoes: LiveData<ArrayList<Shoe>>
        get() = _collectionOfShoes

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
                        _loginSucces.value = true
                    } else {
                        _loginSucces.value = false
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }

    suspend fun getAvailableShoes() {
        var avaialbleShoes = firestore.collection("schoenen")

        var availableShoesForUser: ArrayList<Shoe> = ArrayList()
        try {

            val data = avaialbleShoes
                    .get()
                    .await()
            availableShoesForUser.clear()
            for (currentItem: DocumentSnapshot in data.documents) {
                val shoe = Shoe(currentItem.id, currentItem.getString("name").toString(), currentItem.getString("image").toString(), currentItem.getString("type").toString(), 0)
                availableShoesForUser.add(shoe)
            }
            _shoesForUser.value?.clear()
            _shoesForUser.value = availableShoesForUser
            _retrieveShoesSucces.value = true

        } catch (e: Exception) {
            _retrieveShoesSucces.value = false
            throw e
        }
    }

    fun addShoeToCollection(shoe: Shoe, context: Context) {
        val android_id = getString(context.contentResolver,
                ANDROID_ID)

        val addedShoe = hashMapOf(
                "name" to shoe.shoeName,
                "type" to shoe.shoeType,
                "image" to shoe.shoeImage,
                "milage_coverd" to shoe.milageCovered
        )
        val checkForDuplicate = firestore.collection(android_id).document(shoe.shoeId)
        checkForDuplicate.get()
                .addOnSuccessListener {
                    if (it.getString("name") != shoe.shoeName) {
                        firestore.collection(android_id).document(shoe.shoeId)
                                .set(addedShoe)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Shoe is added to your collection!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { Toast.makeText(context, "Failed to add shoe to your collection. :(", Toast.LENGTH_SHORT).show() }
                    } else {
                        Toast.makeText(context, "You already have this shoe in your collection!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Something went wrong")
                }


    }

    fun updateShoeMilage(shoe: Shoe, context: Context) {
        val android_id = getString(context.contentResolver,
                ANDROID_ID)

        val updatedShoe = hashMapOf(
                "name" to shoe.shoeName,
                "type" to shoe.shoeType,
                "image" to shoe.shoeImage,
                "milage_coverd" to shoe.milageCovered
        )

        firestore.collection(android_id).document(shoe.shoeId)
                .set(updatedShoe)
                .addOnSuccessListener {
                    Log.d(TAG, "Shoe milage is updated" + shoe.milageCovered.toString())
                }
                .addOnFailureListener { Log.e(TAG, "Shoe milage can't be updated") }
    }

    fun addRequestToDatabase(request: String, context: Context) {
        val data = hashMapOf(
                "request" to request
        )
        firestore.collection("shoe_requests").document().set(data)
                .addOnSuccessListener {
                    Toast.makeText(context, "The request has been made!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "The request couldn't be made!", Toast.LENGTH_SHORT).show()

                }
    }

    suspend fun getCollectionOfShoes(context: Context) {
        val android_id = getString(context.contentResolver,
                ANDROID_ID)
        val userCollectionOfShoes: ArrayList<Shoe> = ArrayList()
        val data = firestore.collection(android_id)
        try {
            val data = data
                    .get()
                    .await()

            for (currentItem: DocumentSnapshot in data.documents) {
                userCollectionOfShoes.add(Shoe(currentItem.id, currentItem.getString("name").toString(), currentItem.getString("image").toString(), currentItem.getString("type").toString(), currentItem.getLong("milage_coverd")))
            }
            _collectionOfShoes.value?.clear()
            _collectionOfShoes.value = userCollectionOfShoes

        } catch (e: Exception) {
            throw e
        }
    }
}