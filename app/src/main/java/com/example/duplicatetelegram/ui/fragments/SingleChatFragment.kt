package com.example.duplicatetelegram.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.models.CommonModel
import com.example.duplicatetelegram.utilits.APP_ACTIVITY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * A simple [Fragment] subclass.
 */
class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mainToolBar.toolbar_info.visibility= View.VISIBLE

    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mainToolBar.toolbar_info.visibility= View.GONE
    }
}
