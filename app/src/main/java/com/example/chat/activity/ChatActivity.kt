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
import com.example.chat.RetrofitInstance
import com.example.chat.adapter.ChatAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.model.Chat
import com.example.chat.model.PushNotification
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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



        val userId = intent.getStringExtra("uid")
        val userName = intent.getStringExtra("userName")
        supportActionBar?.title = userName
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnSendMessage.setOnClickListener{
            val message = findViewById<EditText>(R.id.etMessage)
            sendMessage(message.text.toString())
            getMessage()
            message.setText("")
            chatList.clear()

        }
        getUserInfo()

    }

    fun getUserInfo() {

        val intent = intent
        val user = intent.getStringExtra("userName")
        binding.tvUserName.text = user
    }

    fun sendMessage(message: String) {

        val databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = firebaseUser.uid

        hashMap["message"] = message
        databaseReference.push().setValue(hashMap)
        Log.d(TAG, "sendMessage: $message")


    }

    fun getMessage() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (i in p0.children) {
                        val chat = i.getValue(Chat::class.java)
                        chatList.add(chat!!)
                    }
                    val chatAdapter = ChatAdapter(this@ChatActivity,chatList)
                    binding.chatRecyclerView.adapter = chatAdapter
                    binding.chatRecyclerView.scrollToPosition(chatList.size - 1)
                }
            }

        })
    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "sendNotification: ${response.body()}")
            }
            else{
                Log.d(TAG, "sendNotification: ${response.errorBody()}")
            }

        }catch(e: Exception){
            Log.d(TAG, "sendNotification: ${e.message}")
        }
    }

}
