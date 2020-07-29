package com.example.duplicatetelegram.ui.fragments

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.database.*
import com.example.duplicatetelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()
        initFullList()
    }

    private fun initFullList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            settings_input_name.setText(fullnameList[0])
            settings_input_surname.setText(fullnameList[1])
        } else {
            settings_input_name.setText(fullnameList[0])
        }
    }


    override fun change() {
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)




        }
    }


}
