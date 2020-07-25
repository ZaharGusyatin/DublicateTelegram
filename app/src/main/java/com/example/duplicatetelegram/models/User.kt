package com.example.duplicatetelegram.models

data class User(
    var id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var status: String = "",
    var number: String = "",
    var photoUrl: String = "empty"
)