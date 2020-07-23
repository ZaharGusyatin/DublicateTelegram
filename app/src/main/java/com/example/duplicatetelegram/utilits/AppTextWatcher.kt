package com.example.duplicatetelegram.utilits

import android.text.Editable
import android.text.TextWatcher

class AppTextWatcher(val onSucces:(Editable?)->Unit):TextWatcher {
    override fun afterTextChanged(s: Editable?) {
onSucces(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}