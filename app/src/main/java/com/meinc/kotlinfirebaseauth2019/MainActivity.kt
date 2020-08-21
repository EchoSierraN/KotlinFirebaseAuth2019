package com.meinc.kotlinfirebaseauth2019

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int= 5662

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        providers= Arrays.asList<AuthUI.IdpConfig>(

            AuthUI.IdpConfig.EmailBuilder().build(),    //email login
            AuthUI.IdpConfig.FacebookBuilder().build(), //facebook login
            //AuthUI.IdpConfig.GoogleBuilder().build(),   //google login
            AuthUI.IdpConfig.PhoneBuilder().build()     //phone login
        )

        showSignInOptions()

        //event
        btn_sign_out.setOnClickListener{
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled= false
                    showSignInOptions()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== MY_REQUEST_CODE){
            val response= IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                val user= FirebaseAuth.getInstance().currentUser    //get current user
                Toast.makeText(this, "${user?.email}", Toast.LENGTH_SHORT).show()

                btn_sign_out.isEnabled= true
            }
            else {
                Toast.makeText(this, "${response!!.error!!.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(), MY_REQUEST_CODE)
    }
}