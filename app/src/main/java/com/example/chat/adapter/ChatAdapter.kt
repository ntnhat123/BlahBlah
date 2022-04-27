package com.example.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private val MESSAGE_TYPE_LEFT: Int = 1
    private val MESSAGE_TYPE_RIGHT: Int = 2

    private lateinit var firebaseUser: FirebaseUser
    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        if (chatList[position].sender == firebaseUser.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 2) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
//            Toast.makeText(context, "right", Toast.LENGTH_SHORT).show()
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatList = chatList[position]
        if(holder.javaClass == ViewHolder::class.java) {
            holder.txtUserName.text = chatList.message

        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUserName: TextView = itemView.findViewById(R.id.tvMessage)

        val imgUser: ImageView = itemView.findViewById(R.id.userImage)
    }



}