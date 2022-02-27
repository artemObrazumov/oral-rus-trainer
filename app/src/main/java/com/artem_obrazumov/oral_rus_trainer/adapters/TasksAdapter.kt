package com.artem_obrazumov.oral_rus_trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.oral_rus_trainer.Models.TaskSetModel
import com.artem_obrazumov.oral_rus_trainer.R

class TasksAdapter(): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    lateinit var listener: TasksAdapterEventListener
    private var dataSet: ArrayList<TaskSetModel> = ArrayList()

    fun setDataSet(placesList: ArrayList<TaskSetModel>) {
        this.dataSet = placesList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val setName: TextView = view.findViewById(R.id.setName)
        val status: TextView = view.findViewById(R.id.status)
        val eyeIcon: ImageView = view.findViewById(R.id.eyeIcon)
        val context: Context = view.context
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.taskset_row, viewGroup, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.onClick(dataSet[holder.adapterPosition].id)
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val set = dataSet[position]
        viewHolder.setName.text =  set.setName

        val context = viewHolder.context
        val seenSets = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            .getStringSet("seenSets", mutableSetOf<String>())
        if (seenSets!!.contains(set.id)) {
            viewHolder.status.text = "Просмотрено"
            viewHolder.eyeIcon.alpha = 1f
        }
    }

    override fun getItemCount() = dataSet.size

    interface TasksAdapterEventListener {
        fun onClick(taskSetId: String)
    }
}