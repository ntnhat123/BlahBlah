package com.example.chat.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.adapter.ChatAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.model.Chat
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity: AppCompatActivity(){
    private lateinit var binding: ActivityChatBinding
    val TAG = "ChatActivity"
//    var firebaseUser : FirebaseUser? = null
//    var databaseReference : DatabaseReference? = null
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    var chatList = ArrayList<Chat>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val intent = intent
        val userId = intent.getStringExtra("uid")
        val userName = intent.getStringExtra("userName")

        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnSendMessage.setOnClickListener{
            val message = findViewById<EditText>(R.id.etMessage)
            sendMessage(message.text.toString())
            getMessage()
        }
        getUserInfo()

    }

    fun getUserInfo() {

        val intent = intent
        val user = intent.getStringExtra("userName")
        binding.tvUserName.text = user
    }

    fun sendMessage(sendMessage: String) {

        val databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
        val hashMap = HashMap<String, Any>()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        hashMap["sender"] = firebaseUser.uid

        hashMap["message"] = sendMessage
        databaseReference.push().setValue(hashMap)
        Log.d(TAG, "sendMessage: $sendMessage")


    }

    fun getMessage() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                
                chatList.clear()
                for (snapshot in p0.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    chatList.add(chat!!)
                }
                val chatAdapter = ChatAdapter(this@ChatActivity,chatList)
                binding.chatRecyclerView.adapter = chatAdapter
                binding.chatRecyclerView.scrollToPosition(chatList.size - 1)
            }

        })
    }

}
