package com.example.chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.adapter.UserAdapter
import com.example.chat.databinding.ActivityUserBinding
import com.example.chat.firebase.FirebaseData
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceIdReceiver


import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class UserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseData.sharedPref = getSharedPreferences("sharePref", Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().subscribeToTopic("all")
        FirebaseMessaging.getInstance().subscribeToTopic("${FirebaseAuth.getInstance().currentUser!!.uid}")

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.imgProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.imgBack.setOnClickListener {
            finish()

        }
        getUserList()

    }


    fun getUserList(){
        userList = ArrayList()

        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseMessaging.getInstance().subscribeToTopic("topic/$userid")
        val userRef = FirebaseDatabase.getInstance().getReference("/Users")

        userRef.addValueEventListener(object :
            com.google.firebase.database.ValueEventListener {
            override fun onCancelled(snapshot: com.google.firebase.database.DatabaseError) {
            }

            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if(currentUser !!.userImage != ""){ //User model
                    binding.imgProfile.setImageResource(R.drawable.user)

                }else{
                    Glide.with(this@UserActivity).load(currentUser.userImage).into(binding.imgProfile)
                }


                for (datasnapshot in snapshot.children) {
                    val user = datasnapshot.getValue(User::class.java)
                    if (user != null) {
                        if(user.userId != FirebaseAuth.getInstance().currentUser!!.uid){

                            userList.add(user)
                        }
                    }
                }
                val userAdapter = UserAdapter(this@UserActivity, userList)
                val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.userRecyclerView)
                recyclerView.adapter = userAdapter
            }
        })
    }



}