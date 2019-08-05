package com.itis2019.firebasesample.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.itis2019.firebasesample.R
import com.itis2019.firebasesample.adapter.TaskAdapter
import com.itis2019.firebasesample.model.Task
import com.itis2019.firebasesample.utils.Helpers.Companion.validateEditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAdapter: TaskAdapter

    companion object {
        const val TASKS_CHILD = "Tasks"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        } else tv_greeting.text = getString(R.string.user_greeting, firebaseUser.email ?: firebaseUser.phoneNumber)

        val taskRef = initTaskAdapter()
        initListeners(taskRef)
    }

    private fun initListeners(taskRef: CollectionReference) {
        btn_add_task.setOnClickListener {
            val text = ed_add_task.text.toString()
            if (!validateEditText(ed_add_task, text))
                return@setOnClickListener

            val task = Task(text, Calendar.getInstance().time)
            taskRef.add(task)
            ed_add_task.text.clear()
        }

        btn_find_task.setOnClickListener {
            val searchingTask = ed_find_task.text.toString()
            if (!validateEditText(ed_find_task, searchingTask)){
                firebaseAdapter.setQuery(taskRef)
                return@setOnClickListener
            }

            val query = taskRef.whereEqualTo("name", searchingTask)
            firebaseAdapter.setQuery(query)
         }

        btn_sign_out.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        ed_find_task.doAfterTextChanged {  }
    }


    private fun initTaskAdapter(): CollectionReference {
        val manager = LinearLayoutManager(this)

        firestore = FirebaseFirestore.getInstance()
        val taskRef = firestore.collection(TASKS_CHILD)
        val query = taskRef.orderBy("date", Query.Direction.ASCENDING)
        firebaseAdapter = TaskAdapter(query)

        rv_tasks.layoutManager = manager
        rv_tasks.adapter = firebaseAdapter
        return taskRef
    }

    override fun onStart() {
        super.onStart()
        firebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        firebaseAdapter.stopListening()
    }
}
