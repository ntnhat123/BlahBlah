package com.example.chat.activity

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity: AppCompatActivity() , View.OnClickListener{
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var  googleSignInClient: GoogleSignInClient

    companion object {
        const val RC_SIGN_IN = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else {
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
//        val gg =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this,gg)

        auth = FirebaseAuth.getInstance()
        listenView()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_register ->startActivity(Intent(this, RegisterActivity::class.java))
            R.id.btn_login->loginByEmail()
            R.id.tv_forgot_password->startActivity(Intent(this, ForgotPasswordActivity::class.java))
//            R.id.btn_sign_in_google->signIn()
        }
    }

    fun listenView(){
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnSignInGoogle.setOnClickListener(this)

    }
    fun checkForm():Boolean{
        var valid = true
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isEmpty()){
            Toast.makeText(binding.root.context,"Email is required", Toast.LENGTH_SHORT).show()
            valid = false
        }
        if(password.isEmpty()){
            Toast.makeText(binding.root.context,"Password is required", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }
    fun loginByEmail(){

        if(checkForm()){
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            auth = FirebaseAuth.getInstance()
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Vui lòng đợi...")
            progressDialog.show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        startActivity(Intent(this, UserActivity::class.java))
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            binding.root.context,
                            "Login failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(
//                        binding.root.context,
//                        "Google sign in failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }


}