package com.example.chat.adapter

import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.activity.ChatActivity
import com.example.chat.activity.UserActivity
import com.example.chat.model.User

class UserAdapter(private val context: UserActivity, private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = android.view.LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtusername.text= user.userName

        Glide.with(context).load(user.userImage).placeholder(R.drawable.user).into(holder.imgImage)
        holder.cardView.setOnClickListener {
            val intent = android.content.Intent(context, ChatActivity::class.java)
            intent.putExtra("uid", user.userId)

            intent.putExtra("userName", user.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtusername:TextView = itemView.findViewById(R.id.userName)
        val txtTemp : TextView = itemView.findViewById(R.id.temp)
        val imgImage: ImageView = itemView.findViewById(R.id.userImage)

        val cardView = itemView.findViewById<View>(R.id.cardview)

    }
}
