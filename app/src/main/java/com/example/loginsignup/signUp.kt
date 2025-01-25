package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivityMainBinding
import com.example.loginsignup.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class signUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
//  private lateinit var binding: ActivityMainBinding
    private lateinit var binding: ActivitySignUpBinding
    private  lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleAuthCredential: GoogleAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=ActivitySignUpBinding.inflate(layoutInflater)
//      setContentView(R.layout.activity_sign_up)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        auth=Firebase.auth

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this, gso)

        binding.gog.setOnClickListener{
            signInWithGoogle()
        }

////        val gos=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("942154682048-cnc42fp6feaetjfbudo4177g7frrkvkb.apps.googleusercontent.com").requestEmail().build()
//        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id)).requestEmail().build()
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id)) // Correct usage
//            .requestEmail()
//            .build()
////        signInRequest = BeginSignInRequest.builder()
////            .setGoogleIdTokenRequestOptions(
////                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
////                    .setSupported(true)
////                    // Your server's client ID, not your Android client ID.
////                    .setServerClientId(getString(R.string.your_web_client_id))
////                    // Only show accounts previously used to sign in.
////                    .setFilterByAuthorizedAccounts(true)
////                    .build())
////            .build()
//
//        googleSignInClient =GoogleSignIn.getClient(this, gso)
//
//        binding.gog.setOnClickListener{
//            val signInClient=googleSignInClient.signInIntent
//            startActivity(signInClient)
//            launcher.launch(signInClient)
//        }


    }
    private fun signInWithGoogle(){

        val signInIntent=googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
            if (result.resultCode==Activity.RESULT_OK){
                val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            if (account != null){
                Toast.makeText(this, "Successfull outer", Toast.LENGTH_LONG).show()
                updateUI(account)

            }
        }
        else{
            Toast.makeText(this, "Failed outer", Toast.LENGTH_LONG).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
//        showProgressBar()
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Successfull", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,MainActivity2::class.java))
                finish()

            }
            else{
                Toast.makeText(this, "Failed inner", Toast.LENGTH_LONG).show()
            }
        }

    }

//    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//
//        result->
//
//        if (result.resultCode==Activity.RESULT_OK){
//
//            val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
//
//            if(task.isSuccessful){
//                val account:GoogleSignInAccount?=task.result
//                val credential=GoogleAuthProvider.getCredential(account?.idToken, null)
//                auth.signInWithCredential(credential).addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        Toast.makeText(this, "SuccessFull", Toast.LENGTH_LONG).show()
//                    }
//                    else{
//                        val exception= task.exception
//                        Toast.makeText(this, "Failed inner", Toast.LENGTH_LONG).show()
//                    }
//                }
//
//            }
//        }
//        else{
//            Toast.makeText(this, "Failed outer", Toast.LENGTH_LONG).show()
//        }
//    }
}