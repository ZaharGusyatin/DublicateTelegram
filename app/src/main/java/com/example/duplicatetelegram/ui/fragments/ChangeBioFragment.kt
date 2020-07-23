package com.example.duplicatetelegram.ui.fragments

import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_bio.*

/**
 * A simple [Fragment] subclass.
 */
class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {
    override fun onStart() {
        super.onStart()
        settings_input_bio.setText(USER.bio)

    }

    override fun change() {
        super.change()
        val newBio=settings_input_bio.text.toString()
        REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    USER.bio=newBio
                    fragmentManager?.popBackStack()
                }
            }
    }
}
