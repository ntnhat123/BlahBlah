package com.example.chat.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    val TAG = "ChatActivity"
//    var firebaseUser : FirebaseUser? = null
//    var databaseReference : DatabaseReference? = null
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            finish()
        }
        val intent=intent
        val user = intent.getStringExtra("uid")



        getUserInfo()


    }

    fun getUserInfo() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("/Users").child(firebaseUser.uid)
        databaseReference.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                Log.d(TAG, "onDataChange: " + user!!.userName)
                binding.tvUserName.text = user.userName

                if (user.userImage.equals("default")){
                    binding.imgProfilechat.setImageResource(R.drawable.user)
                } else {
                    Glide.with(this@ChatActivity).load(user.userImage).into(binding.imgProfilechat)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ChatActivity, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}
