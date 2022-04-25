package com.example.chat.activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.R
import com.example.chat.databinding.ActivityForgotpassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgotpassBinding
    private lateinit var FirebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    companion object {
        const val RC_SIGN_IN = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else {
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        listenerViewForgotPassword()

    }
    fun listenerViewForgotPassword(){
        binding.btnForgotPassword.setOnClickListener(this)

    }

    fun checkForm():Boolean{
        var valid = true
        val emailforgot = binding.etEmailforgot.text.toString()
        if (emailforgot.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailforgot).matches()) {
            binding.etEmailforgot.error = "Nhập địa chỉ email"
            valid = false
        } else {
            binding.etEmailforgot.error = null
        }
        return valid
    }

    private fun validateData(){
        if(checkForm()){
            FirebaseAuth = Firebase.auth

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Vui lòng chờ...")
            progressDialog.show()
            FirebaseAuth.sendPasswordResetEmail(binding.etEmailforgot.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        finish()
                    } else {
                        progressDialog.dismiss()
                        binding.etEmailforgot.error = "Không tìm thấy email"
                    }
                }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_forgot_password -> {
                validateData()
            }
        }
    }


}