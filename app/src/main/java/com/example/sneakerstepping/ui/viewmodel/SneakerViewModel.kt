package com.example.sneakerstepping.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class SneakerViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var auth: FirebaseAuth

}