package com.example.duplicatetelegram.ui.fragments.single_chat

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.models.CommonModel
import com.example.duplicatetelegram.models.UserMode
import com.example.duplicatetelegram.ui.fragments.BaseFragment
import com.example.duplicatetelegram.utilits.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceiving: UserMode
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppValueEventListener
    private var mListMessages = emptyList<CommonModel>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        mRecyclerView = single_chat_recyclerView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATA_BASE_ROOT.child(NODE_MESSAGE)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mMessagesListener = AppValueEventListener { DataSnaphot ->
            mListMessages = DataSnaphot.children.map { it.getCommonModel() }
            mAdapter.setList(mListMessages)
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRefMessages.addValueEventListener(mMessagesListener)
    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mainToolBar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceiving = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATA_BASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        single_chat_btn_send_message.setOnClickListener {
            val message = single_chat_input_message.text.toString()
            if (message.isEmpty()) {
                showToast(getString(R.string.send_message_plz))
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                single_chat_input_message.setText("")
            }
        }
    }


    private fun initInfoToolbar() {
        if (mReceiving.fullname.isEmpty()) {
            mToolbarInfo.single_chat_fullname.text = contact.fullname
        } else
            mToolbarInfo.single_chat_fullname.text = mReceiving.fullname
        mToolbarInfo.single_chat_photo.downloadAndSetImage(mReceiving.photoUrl)
        mToolbarInfo.single_chat_status.text = mReceiving.status
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mMessagesListener)
    }
}
