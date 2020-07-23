package com.example.duplicatetelegram.utilits

import android.net.Uri
import com.example.duplicatetelegram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATA_BASE_ROOT: DatabaseReference
lateinit var USER: User
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var CURRENT_UID: String

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_STATUS = "status"
const val CHILD_PHONE = "number"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATA_BASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}
 inline fun putUrlDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(
        CHILD_PHOTO_URL
    ).setValue(url).addOnSuccessListener {
        function()
    }.addOnFailureListener { showToast(it.message.toString()) }

}


inline fun getUrlFromStorage(path: StorageReference,crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {
            function(it.toString())
        }.addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri?, path: StorageReference,crossinline function: () -> Unit) {
    path.putFile(uri!!).addOnSuccessListener {
        function()
    }.addOnFailureListener { showToast(it.message.toString()) }
}
inline fun initUser(crossinline function: () -> Unit) {
    REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(USER::class.java) ?: User()
            if (USER.username.isEmpty()){
                USER.username= CURRENT_UID
            }
            function()
        })
}