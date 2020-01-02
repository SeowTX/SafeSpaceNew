package com.example.safespace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.buttonLogin
import kotlinx.android.synthetic.main.activity_main.buttonRegister
import kotlinx.android.synthetic.main.activity_register.*
import android.graphics.Color;
import android.view.inputmethod.InputMethodManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.database.FirebaseDatabase
import android.app.Activity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class RegisterActivity: AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_register)





        buttonRegister.setOnClickListener {
            registerAccount()
        }


            buttonLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

    }

    private fun registerAccount() {


        // Getting registration details
        var username = editTextEmail.text.toString().trim()
        var email = editTextEmail.text.trim().toString().trim()
        var password = editTextPass.text.trim().toString().trim()
        var confirmPassword = editTextConfirmPass.text.trim().toString().trim()



        if (!password.equals(confirmPassword) || username.length < 6 || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            if(username.length<6)
                showError("Registration", "Username must be at least 6 characters")
            else if(password.isEmpty() || confirmPassword.isEmpty()){
                showError("Registration", "Password cannot be empty")
            }
            else if(email.isEmpty()){
                showError("Registration", "Email address cannot be empty")
            }
            else if(!password.equals(confirmPassword))
                showError("Registration", "Password and confirm password must match")



        } else {

            // Show progress bar
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Loading"
            pDialog.setCancelable(false)
            pDialog.show()

            // Connect to Firebase Auth
            var auth = FirebaseAuth.getInstance()


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Register Activity", "createUserWithEmail:success")
                        val user = auth.currentUser


                        // Hide loading animation
                        pDialog.dismiss()

                        // Add new user to firebase database
                        val userId = FirebaseAuth.getInstance().uid ?: ""
                        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")
                        val newUser = User(userId, username, email)


                        ref.setValue(newUser).addOnSuccessListener {
                            SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registration")
                                .setContentText("Your account has been created")
                                .setConfirmClickListener {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                .show()
                        }





                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Register Activity", "createUserWithEmail:failure", task.exception)


                        // Hide Animation
                        pDialog.dismiss()


                        showError("Registration", task.exception.toString())
                    }
                }
        }
    }

    private fun showError(title:String, msg: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(title)
            .setContentText(msg)
            .show()
    }
    private fun showSuccess(title: String, msg: String){

    }
}

class User(val userId: String, val username: String, val email: String)