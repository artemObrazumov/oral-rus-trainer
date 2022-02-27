package com.artem_obrazumov.oral_rus_trainer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.oral_rus_trainer.Models.TaskSetModel
import com.artem_obrazumov.oral_rus_trainer.adapters.TasksAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var adapter: TasksAdapter = TasksAdapter()
    private lateinit var list: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        list = findViewById(R.id.taskSetList)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        initializeAdapter()
        getAdapterData()
    }

    private fun initializeAdapter() {
        adapter.listener = object: TasksAdapter.TasksAdapterEventListener{
            override fun onClick(taskSetId: String) {
                val seenSets = baseContext.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    .getStringSet("seenSets", mutableSetOf<String>())
                seenSets!!.add(taskSetId)
                with (
                    baseContext.getSharedPreferences("user_data", MODE_PRIVATE).edit()
                ) {
                    putStringSet("seenSets", seenSets)
                    apply()
                }

                val intent = Intent(baseContext, TaskActivity::class.java)
                intent.putExtra("TaskId", taskSetId)
                startActivity(intent)
            }
        }
    }

    private fun getAdapterData() {
        FirebaseDatabase.getInstance().getReference("taskSets").addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val setsList = ArrayList<TaskSetModel>()
                    for (data in snapshot.children) {
                        val taskSet = data.getValue(TaskSetModel::class.java)!!
                        setsList.add(taskSet)
                    }
                    adapter.setDataSet(setsList)
                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}