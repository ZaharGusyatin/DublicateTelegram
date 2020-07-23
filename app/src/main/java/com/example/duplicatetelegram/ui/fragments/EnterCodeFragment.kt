package com.example.duplicatetelegram.ui.fragments

import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.activities.MainActivity
import com.example.duplicatetelegram.activities.RegisterActivity
import com.example.duplicatetelegram.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

/**
 * A simple [Fragment] subclass.
 */
class EnterCodeFragment(val phoneNumber: String, val id: String?) :
    BaseFragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        AUTH = FirebaseAuth.getInstance()
        registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = registerInputCode.text.toString()
            if (string.length == 6) {
                verifiCode()
            }

        })
    }

     fun verifiCode() {
        val code = registerInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id.toString(), code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                var dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME] = uid
                REF_DATA_BASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            showToast("welcome")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        } else {
                            showToast(it.exception?.message.toString())
                        }
                    }

            } else {
                showToast(it.exception?.message.toString())
            }

        }
    }

}
