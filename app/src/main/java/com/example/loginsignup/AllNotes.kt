package com.example.loginsignup

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivityAllNotesBinding
import com.example.loginsignup.databinding.NoteItemBinding
import com.example.loginsignup.databinding.UpdateNoteBinding
import com.google.android.play.integrity.internal.l
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotes : AppCompatActivity(), noteAdapter.OnItemClickListener {
    private  val binding:ActivityAllNotesBinding by lazy {
        ActivityAllNotesBinding.inflate(layoutInflater)
    }
    private  lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseReference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()
        recyclerView=binding.rvNote
        recyclerView.layoutManager= LinearLayoutManager(this)

        val currentUser=auth.currentUser

        currentUser?.let { user->
            val noteReference:DatabaseReference=databaseReference.child("users").child(user.uid).child("notes")

            noteReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                  val  noteList= mutableListOf<NotesItem>()

                    for (noteSnapshot in snapshot.children){
                        val note=noteSnapshot.getValue(NotesItem::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    val adapter=noteAdapter(noteList,this@AllNotes)
                    recyclerView.adapter=adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }



    }

    override fun onDeleteClick(noteId: String) {
        val currentUser=auth.currentUser
        currentUser?.let{ user->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.child(noteId).removeValue()
        }


    }

    override fun onUpdateClick(noteId: String, currentTitle: String, currentDes: String) {

        val dialogBinding = UpdateNoteBinding.inflate(LayoutInflater.from(this));
        val dialog=AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Note")
            .setPositiveButton("Update"){dialog , _->
                val newTitle=dialogBinding.editTitle.text.toString()
                val newDescriptor=dialogBinding.editDes.text.toString()
                updateNoteDatabase(noteId,newTitle,newDescriptor)
                dialog.dismiss()
            }
            .setNegativeButton("Cancle1"){dialog ,_ ->
                dialog.dismiss()
            }
            .create()
        dialogBinding.editTitle.setText(currentTitle)
        dialogBinding.editDes.setText(currentDes)

        dialog.show()



    }

    private fun updateNoteDatabase(noteId: String, newTitle: String, newDescriptor: String) {
        val currentUser= auth.currentUser
        currentUser?.let{ user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote=NotesItem(newTitle,newDescriptor,noteId)

            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener {  task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Update successful",Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this,"Failed to Update Note",Toast.LENGTH_SHORT).show()

                    }

                }

        }
    }
}