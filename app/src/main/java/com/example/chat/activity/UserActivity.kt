package com.example.chat.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.adapter.UserAdapter
import com.example.chat.databinding.ActivityUserBinding

import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver


import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList


class UserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    var userList = ArrayList<User>()
    var newArrayList= ArrayList<User>()

    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseMessaging.getInstance().subscribeToTopic("all")
        FirebaseMessaging.getInstance().subscribeToTopic("${FirebaseAuth.getInstance().currentUser!!.uid}")

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))

        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByUsername(query.toString())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userList.clear()
                val searchtext = newText!!.toLowerCase(Locale.getDefault())
                if(searchtext.isNotEmpty()){

                    for(user in newArrayList){
                        if(user.userName.toLowerCase(Locale.getDefault()).contains(searchtext)){
                            userList.add(user)
                        }
                    }
                    binding.userRecyclerView.adapter?.notifyDataSetChanged()
                }
                else{
                    userList.clear()
                    userList.addAll(newArrayList)
                    binding.userRecyclerView.adapter?.notifyDataSetChanged()
                }
                return false
            }

        })

        getUserList()

    }
    fun searchByUsername(userName: String){
        val userRef = FirebaseDatabase.getInstance().getReference("/Users")
        userRef.addValueEventListener(object :
            com.google.firebase.database.ValueEventListener {
            override fun onCancelled(snapshot: com.google.firebase.database.DatabaseError) {
            }

            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                userList.clear()
                if(snapshot.exists()){
                    for (snap in snapshot.children){
                        val user = snap.getValue(User::class.java)
                        if(user!!.userName.toLowerCase().contains(userName.toLowerCase())){
                            userList.add(user)
                        }
                    }
                    val adapter = UserAdapter(this@UserActivity, userList)
                    binding.userRecyclerView.adapter = adapter
                }else{
                    Toast.makeText(this@UserActivity, "No user found", Toast.LENGTH_LONG).show()
                }

            }
        })

    }


    fun getUserList(){



        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseMessaging.getInstance().subscribeToTopic("topic/$userid")
        val userRef = FirebaseDatabase.getInstance().getReference("/Users")

        userRef.addValueEventListener(object :
            com.google.firebase.database.ValueEventListener {
            override fun onCancelled(snapshot: com.google.firebase.database.DatabaseError) {
            }

            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                userList.clear()


                for (datasnapshot in snapshot.children) {
                    val user = datasnapshot.getValue(User::class.java)
                    if (user != null) {
                        if(user.userId != FirebaseAuth.getInstance().currentUser!!.uid){
                            userList.add(user)
                        }
                    }
                }
                newArrayList.addAll(userList)
                val userAdapter = UserAdapter(this@UserActivity, userList)
                val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.userRecyclerView)
                recyclerView.adapter = userAdapter
            }
        })
    }




}