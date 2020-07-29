package com.example.duplicatetelegram.ui.fragments

import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.database.*
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
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)


    }


}
