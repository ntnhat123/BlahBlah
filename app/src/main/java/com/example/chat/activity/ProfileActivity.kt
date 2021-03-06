package com.example.chat.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.databinding.ActivityProfileBinding
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.view.View
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import android.view.View.GONE as ViewGONE

class ProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var ImageUri: Uri


    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProfile()
        getImageUri()


    }

    fun getImageUri(){
        binding.imgProfile2.setOnClickListener{
            selectImage()
        }

        binding.btnSaveProfile.setOnClickListener{
            uploadImage()
            binding.progressBar.visibility = VISIBLE

        }
        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        binding.tvUserProfile.setOnClickListener{
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

    }

    fun getProfile(){
        val etUserProfile = binding.etUserProfile
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference  = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        databaseReference.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                Toast.makeText(this@ProfileActivity, snapshot.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                etUserProfile.setText(user!!.userName)
//

            }

        })
    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }
    private fun uploadImage(){
        val progress = ProgressDialog(this)
        progress.setTitle("Vui l??ng ch???...")
        progress.setCancelable(false)
        progress.show()

        val formatter = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
        val currentDate = formatter.format(Date())
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$currentDate.jpg")
        storageRef.putFile(ImageUri).addOnSuccessListener {
            binding.imgProfile2.setImageURI(ImageUri)

            Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show()
            if (progress.isShowing)
                progress.dismiss()
            binding.progressBar.visibility = ViewGONE
            binding.btnSaveProfile.visibility = ViewGONE
            storageRef.downloadUrl.addOnSuccessListener {
                saveUserToStorage(it.toString())

            }
            getImageProfile(it.toString())

        }.addOnFailureListener {
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            if (progress.isShowing)
                progress.dismiss()
        }
    }
    fun saveUserToStorage(userImage: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        val user = User(firebaseUser.uid,binding.etUserProfile.text.toString(),userImage)
        databaseReference.setValue(user)


    }
    fun getImageProfile(profileImage: String){
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$profileImage")
        storageRef.downloadUrl.addOnSuccessListener {
            binding.imgProfile2.setImageURI(it)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            ImageUri = data?.data!!
            binding.imgProfile2.setImageURI(ImageUri)
            binding.btnSaveProfile.visibility = VISIBLE
        }
    }

}