package com.assistne.kotlintodoapp.data.source.remote

import android.os.Handler
import android.os.Looper
import com.assistne.kotlintodoapp.data.source.TasksDataSource
import com.assistne.kotlintodoapp.tasks.domain.model.Task
import java.util.*

/**
 * Created by assistne on 17/2/20.
 */
class TasksRemoteDataSource private constructor() : TasksDataSource {

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        // Simulate network by delaying the execution.
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            callback.onTasksLoaded(ArrayList(TASKS_SERVICE_DATA.values))
        }, SERVICE_LATENCY_IN_MILLIS.toLong())
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = TASKS_SERVICE_DATA[taskId]
        if (task != null) {
            val handler = Handler()
            handler.postDelayed({
                callback.onTaskLoaded(task)
            }, SERVICE_LATENCY_IN_MILLIS.toLong())
        }
    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, task.id, true)
        TASKS_SERVICE_DATA[task.id] = completedTask
    }

    override fun completeTask(taskId: String) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, task.id)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun clearCompletedTasks() {
        val it = TASKS_SERVICE_DATA.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            if (entry.value.isCompleted) {
                it.remove()
            }
        }
    }

    override fun refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    object Holder {
        val INSTANCE: TasksRemoteDataSource = TasksRemoteDataSource()
    }

    companion object {
        private val SERVICE_LATENCY_IN_MILLIS = 5000

        private val TASKS_SERVICE_DATA: MutableMap<String, Task> = LinkedHashMap(2)

        init {
            addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
            addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
        }

        fun getInstance(): TasksRemoteDataSource = Holder.INSTANCE

        private fun addTask(title: String, description: String) {
            val newTask = Task(title, description)
            TASKS_SERVICE_DATA.put(newTask.id, newTask)
        }
    }
}
