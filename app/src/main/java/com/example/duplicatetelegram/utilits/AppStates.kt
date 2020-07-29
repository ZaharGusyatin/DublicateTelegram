package com.example.duplicatetelegram.utilits

import com.example.duplicatetelegram.database.*

enum class AppStates(val status:String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPENG("печатает");

    companion object{
        fun updateState(appStates:AppStates){
            if(AUTH.currentUser!=null){
                REF_DATA_BASE_ROOT.child(
                    NODE_USERS
                ).child(CURRENT_UID).child(
                    CHILD_STATUS
                ).setValue(appStates.status)
                    .addOnSuccessListener { USER.status=appStates.status }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }

        }
    }
}