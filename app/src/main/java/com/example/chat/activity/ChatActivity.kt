package com.example.chat.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.adapter.ChatAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.model.Chat
import com.example.chat.model.PushNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity: AppCompatActivity(){
    private lateinit var binding: ActivityChatBinding
    val TAG = "ChatActivity"
    var topic = ""
    val SimpleDateFormat = "dd/MM/yyyy HH:mm"


    //    var firebaseUser : FirebaseUser? = null
//    var databaseReference : DatabaseReference? = null
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    var chatList = ArrayList<Chat>()


    @RequiresApi(Build.VERSION_CODES.O)
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


            if (userId != null) {
                sendMessage(message.text.toString(), userId)
            }
            if (userId != null) {
                getMessage(it.toString(), userId)
            }
            message.setText("")
            chatList.clear()
            topic = "topic_" + userId

            getImage()

        }


        // send image
//        val pickImageContracts = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            Log.d(TAG, "uri: $uri")
//            val storageReference = FirebaseStorage.getInstance().reference.child("chat_images")
//            val imageName = UUID.randomUUID().toString()
//            val imageRef = storageReference.child(imageName)
//            imageRef.putFile(uri).addOnSuccessListener {
//                Log.d(TAG, "success")
//                imageRef.downloadUrl.addOnSuccessListener {
//                    Log.d(TAG, "downloadUrl: $it")
//                    val message = findViewById<EditText>(R.id.etMessage)
//                    sendMessage(message.text.toString())
//                    getMessage()
//                    message.setText("")
//                    chatList.clear()
//                    topic = "topic_" + userId
//                    PushNotification(NotificationData(userName!!,message.text.toString()),topic).also {
//                        sendNotification(it)
//
//                    }
//                    getImage()
//                }
//            }
//        }
//        binding.imgSend.setOnClickListener {
//            pickImageContracts.launch("image/*")
//
//        }

        getUserInfo()


    }
    fun sendMessage(message: String,receiverId:String) {

        if(message.isNotEmpty()) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val hashMap = HashMap<String, Any>()
            hashMap["sender"] = firebaseUser.uid
            hashMap["receiverId"] = receiverId
            hashMap["message"] = message
            databaseReference.push().setValue(hashMap)
            Log.d(TAG, "sendMessage: $message")
        }else{
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }


    }

    fun getMessage(receiverId:String,sender:String){
        databaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatList.clear()
                if (p0.exists()) {
                    for (i in p0.children) {
                        val chat = i.getValue(Chat::class.java)
                        if(chat?.receiverId.equals(firebaseUser.uid) && chat?.sender.equals(sender) ||
                            chat?.receiverId.equals(sender) && chat?.sender.equals(firebaseUser.uid) ||
                                    chat?.receiverId.equals(firebaseUser.uid) && chat?.sender.equals(receiverId)
                        ){
                            chatList.add(chat!!)
                        }

//                        chatList.add(chat!!)
//                        if (chat!!.receiverId == firebaseUser.uid && chat.sender ==  sender|| chat.receiverId == firebaseUser.uid && chat.sender == receiverId) {
//                            chatList.add(chat)
//                        }
//                        chatList.add(chat!!)

                    }
                    val chatAdapter = ChatAdapter(this@ChatActivity,chatList)
                    binding.chatRecyclerView.adapter = chatAdapter
                    binding.chatRecyclerView.scrollToPosition(chatList.size - 1)


                }
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }



    fun getUserInfo() {
        val intent = intent
        val user = intent.getStringExtra("userName")
        binding.tvUserName.text = user
    }



    fun getImage(){
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val imageRef = storageReference.child("images/"+firebaseUser.uid+".jpg")
        imageRef.downloadUrl.addOnSuccessListener {
            Log.d("TAG", "onSuccess: $it")
        }.addOnFailureListener {
            Log.d("TAG", "onFailure: $it")
        }
    }


}

