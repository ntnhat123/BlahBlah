package com.example.chat.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.R
import com.example.chat.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else {
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        listtenView()

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_register -> registerUser()
        }
    }
    fun listtenView(){
        binding.btnRegister.setOnClickListener(this)

    }

    fun checkform(): Boolean {
        var valid = true
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmpassword.text.toString()
        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            valid = false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            valid = false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            valid = false
        }
        if (confirmPassword.isEmpty()) {
            binding.etConfirmpassword.error = "Confirm Password is required"
            valid = false
        }
        if (password != confirmPassword) {
            binding.etConfirmpassword.error = "Password does not match"
            valid = false
        }
        return valid
    }
    fun registerUser() {
        if(checkform()){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Đăng ký")
            progressDialog.setMessage("Vui lòng đợi...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            val     name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmpassword.text.toString()
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    progressDialog.dismiss()
                                    finish()
                                }
                            }
                        val userId:String = user!!.uid

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user?.uid.toString())
                        var hashMap = HashMap<String, Any>()
                        hashMap.put("uid", userId)
                        hashMap.put("userName", name)
                        hashMap.put("email", email)
                        hashMap.put("password", password)
                        hashMap.put("confirmPassword", confirmPassword)
                        hashMap.put("profileImageUrl", "default")
                        databaseReference.setValue(hashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                 progressDialog.dismiss()
                                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                        }

                    } else {
                        progressDialog.dismiss()
                        binding.etEmail.error = "Email already exists"
                    }
                }
        }
    }



}




