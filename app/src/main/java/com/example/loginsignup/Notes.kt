package com.example.loginsignup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivityNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.Reference

class Notes : AppCompatActivity() {
    private val binding:ActivityNotesBinding by lazy {
        ActivityNotesBinding.inflate(layoutInflater)
    }
    private lateinit var databaaeReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_notes)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaaeReference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()

        binding.btnSave.setOnClickListener {
            val title= binding.etTitle.text.toString()
            val  des= binding.etDes.text.toString()

            if(title.isEmpty() && des.isEmpty()){
                Toast.makeText(this, "fill title and description", Toast.LENGTH_SHORT).show()
            }
            else{
                val currentUser=auth.currentUser
                currentUser ?. let { user ->
//                    Generate a unique key
                    val noteKey= databaaeReference.child("users").child(user.uid).child("notes").push().key

                    val notesItem=NotesItem(title,des,noteKey?:"")
                    if (noteKey != null){
                        databaaeReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(notesItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }

                            }
                    }
                }

            }


        }
    }
}