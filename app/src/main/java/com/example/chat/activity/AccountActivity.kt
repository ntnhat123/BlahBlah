package com.example.chat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.databinding.ActivityAccountBinding
import com.example.chat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AccountActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenView()

    }

    fun listenView(){
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

//    fun readAccount(name: String) {
//        database = FirebaseDatabase.getInstance().reference.child("Users").child(name)
//        database.child(name).get().addOnSuccessListener {
//            if (it.exists()) {
//                val user = User(it.child("userName").value.toString(), it.child("email").value.toString())
//
//            }
//        }
//
//    }
}