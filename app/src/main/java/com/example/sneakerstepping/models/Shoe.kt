package com.example.sneakerstepping.models

data class Shoe(
    var shoeId: String,
    var shoeName: String,
    var shoeImage: String,
    var shoeType: String,
    var milageCovered: Long?
) {
}