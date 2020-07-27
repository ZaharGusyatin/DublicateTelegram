package com.example.duplicatetelegram.utilits

import android.net.Uri
import android.provider.ContactsContract
import com.example.duplicatetelegram.models.CommonModel
import com.example.duplicatetelegram.models.UserMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATA_BASE_ROOT: DatabaseReference
lateinit var USER: UserMode
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var CURRENT_UID: String

const val TYPE_TEXT = "text"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_NUMBERS = "numbers"
const val NODE_MESSAGE = "messages"
const val NODE_NUMBERS_CONTACTS = "numbers_contacts"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_STATUS = "status"
const val CHILD_PHONE = "number"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"

const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATA_BASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserMode()
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


inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {
            function(it.toString())
        }.addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri?, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri!!).addOnSuccessListener {
        function()
    }.addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(USER::class.java) ?: UserMode()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
}


fun updateNumbersToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATA_BASE_ROOT.child(NODE_NUMBERS)
            .addListenerForSingleValueEvent(AppValueEventListener {
                it.children.forEach { snapshot ->
                    arrayContacts.forEach { contact ->
                        if (snapshot.key == contact.number) {
                            REF_DATA_BASE_ROOT.child(NODE_NUMBERS_CONTACTS).child(CURRENT_UID)
                                .child(snapshot.value.toString()).child(CHILD_ID)
                                .setValue(snapshot.value.toString())
                                .addOnFailureListener { showToast(it.message.toString()) }//добавление контактов в бд

                            REF_DATA_BASE_ROOT.child(NODE_NUMBERS_CONTACTS).child(CURRENT_UID)
                                .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                                .setValue(contact.fullname)
                                .addOnFailureListener { showToast(it.message.toString()) }//добавление контактов в бд
                        }
                    }
                }
            })
    }

}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserMode =
    this.getValue(UserMode::class.java) ?: UserMode()

fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGE/$CURRENT_UID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGE/$receivingUserID/$CURRENT_UID"
    val messageKey = REF_DATA_BASE_ROOT.child(refDialogUser).push().key
    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATA_BASE_ROOT
        .updateChildren(mapDialog).addOnSuccessListener {
            function()
        }.addOnFailureListener { showToast(it.message.toString()) }
}
