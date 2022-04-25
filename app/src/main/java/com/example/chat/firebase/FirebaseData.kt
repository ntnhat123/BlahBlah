package com.example.chat.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.activity.ChatActivity

class FirebaseData {
    companion object {
        fun getIntent(activity: AppCompatActivity): Intent {
            return Intent(activity, ChatActivity::class.java)
        }
    }
}