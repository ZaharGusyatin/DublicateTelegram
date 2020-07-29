package com.example.duplicatetelegram.ui.fragments.register

import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.database.AUTH
import com.example.duplicatetelegram.utilits.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onStart() {
        super.onStart()
        AUTH = FirebaseAuth.getInstance()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("welcome")
                        restartActivity()
                    } else {
                        showToast(it.exception?.message.toString())
                    }

                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0?.message.toString())
            }


            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(
                    EnterCodeFragment(
                        mPhoneNumber,
                        id.toString()
                    )
                )

            }
        }
        registerBtnNext.setOnClickListener {
            sendCode()
        }

    }

    private fun sendCode() {
        if (registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.write_number))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = registerInputPhoneNumber.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS, APP_ACTIVITY,
            mCallBack
        )
    }
}
