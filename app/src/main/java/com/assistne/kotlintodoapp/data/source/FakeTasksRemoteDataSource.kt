package com.assistne.kotlintodoapp.data.source

import android.support.annotation.VisibleForTesting
import com.assistne.kotlintodoapp.tasks.domain.model.Task
import java.util.*

/**
 * Created by assistne on 17/2/20.
 */
class FakeTasksRemoteDataSource private constructor(): TasksDataSource {
    companion object {
        var INSTANCE: FakeTasksRemoteDataSource? = null
        val TASKS_SERVICE_DATA by lazy { LinkedHashMap<String, Task>() }
        fun getInstance() : FakeTasksRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = FakeTasksRemoteDataSource()
            }
            return INSTANCE as FakeTasksRemoteDataSource
        }
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        callback.onTasksLoaded(ArrayList(TASKS_SERVICE_DATA.values))
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = TASKS_SERVICE_DATA[taskId]
        if (task != null) {
            callback.onTaskLoaded(task)
        }
    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, task.id, true)
        TASKS_SERVICE_DATA.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {

    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, task.id)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {

    }

    override fun clearCompletedTasks() {
        for ((key, value) in TASKS_SERVICE_DATA) {
            if (value.isCompleted) {
                TASKS_SERVICE_DATA.remove(key)
            }
        }
    }

    override fun refreshTasks() {

    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    @VisibleForTesting
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            TASKS_SERVICE_DATA.put(task.id, task)
        }
    }
}