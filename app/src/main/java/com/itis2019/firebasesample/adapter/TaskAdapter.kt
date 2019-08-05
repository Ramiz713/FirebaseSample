package com.itis2019.firebasesample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.itis2019.firebasesample.R
import com.itis2019.firebasesample.model.Task
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_task.view.*
import java.text.SimpleDateFormat


class TaskAdapter(query: Query) :
        FirestoreAdapter< TaskAdapter.TaskHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder =
            TaskHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_task,
                            parent,
                            false
                    )
            )


    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val message = getSnapshot(position).toObject(Task::class.java) ?: return

        holder.bind(message)
    }

    class TaskHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(task: Task) =
                with(containerView) {
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    tv_name.text = context.getString(R.string.task_name, task.name)
                    tv_date.text = context.getString(R.string.task_date, dateFormat.format(task.date))
                }
    }
}
