package com.example.duplicatetelegram.utilits

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppChildEventListener(val onSucces:(DataSnapshot)->Unit) : ChildEventListener {
    override fun onCancelled(error: DatabaseError) {
      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
    onSucces(snapshot)

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}