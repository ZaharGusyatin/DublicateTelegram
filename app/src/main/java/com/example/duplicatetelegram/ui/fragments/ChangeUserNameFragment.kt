package com.example.duplicatetelegram.ui.fragments

import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChangeUserNameFragment : BaseChangeFragment(R.layout.fragment_change_user_name) {
    lateinit var mNewUserName: String
    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }


    override fun change() {
        mNewUserName = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUserName.isEmpty()) {
            showToast("поле пустое")
        } else {
            REF_DATA_BASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUserName)) {
                        showToast("Такой пользователь уже есть")
                    } else {
                        changeUserName2()
                    }
                })
        }

    }

    private fun changeUserName2() {
        REF_DATA_BASE_ROOT.child(NODE_USERNAMES).child(mNewUserName)
            .setValue(CURRENT_UID).addOnCompleteListener {
                if (it.isSuccessful) {
                    changeUserName3()
                }
            }

    }

    private fun changeUserName3() {
        REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(mNewUserName).addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    deleteOldUserName()

                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUserName() {
        REF_DATA_BASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    fragmentManager?.popBackStack()
                    USER.username = mNewUserName

                } else {
                    showToast(it.exception?.message.toString())
                }

            }
    }
}
