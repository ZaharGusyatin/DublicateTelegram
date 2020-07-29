package com.example.duplicatetelegram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.models.CommonModel
import com.example.duplicatetelegram.database.CURRENT_UID
import com.example.duplicatetelegram.utilits.DiffUtilCallback
import com.example.duplicatetelegram.utilits.asTime
import kotlinx.android.synthetic.main.message_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<CommonModel>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blocUserMessage: ConstraintLayout = view.bloc_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.chat_user_time

        val blocReceivedMessage: ConstraintLayout = view.bloc_receiving_message
        val chatReceivedMessage: TextView = view.chat_receiving_message
        val chatReceivedMeessageTime: TextView = view.chat_receiving_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMeessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }


    fun addItem(item: CommonModel) {
        val newList = mutableListOf<CommonModel>()
        newList.addAll(mListMessagesCache)

        if (!newList.contains(item)) newList.add(item)

        newList.sortBy { it.timeStamp.toString() }
        mDiffResult = DiffUtil.calculateDiff(DiffUtilCallback(mListMessagesCache, newList))
        mDiffResult.dispatchUpdatesTo(this)
        mListMessagesCache = newList
    }
}
