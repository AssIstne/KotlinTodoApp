package com.assistne.kotlintodoapp.data.source

import com.assistne.kotlintodoapp.tasks.domain.model.Task
import java.util.*

/**
 * Created by assistne on 17/2/20.
 */
class TasksRepository private constructor(val tasksRemoteDataSource: TasksDataSource, val tasksLocalDataSource: TasksDataSource): TasksDataSource {
    val cachedTasks: MutableMap<String, Task> by lazy { LinkedHashMap<String, Task>() }
    var cacheIsDirty = false
    companion object {
        var INSTANCE: TasksRepository? = null
        fun getInstance(tasksRemoteDataSource: TasksDataSource, tasksLocalDataSource: TasksDataSource) : TasksRepository {
            if (INSTANCE == null) {
                INSTANCE = TasksRepository(tasksRemoteDataSource, tasksLocalDataSource)
            }
            return INSTANCE as TasksRepository
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        if (!cacheIsDirty) {
            callback.onTasksLoaded(ArrayList(cachedTasks.values))
            return
        }

        if (cacheIsDirty) {
            getTasksFromRemoteDataSource(callback);
        } else {
            tasksLocalDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {
                    refreshCache(tasks)
                    callback.onTasksLoaded(ArrayList(cachedTasks.values))
                }

                override fun onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val cachedTask = getTaskWithId(taskId)
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask)
            return
        }

        tasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                callback.onTaskLoaded(task)
            }

            override fun onDataNotAvailable() {
                tasksRemoteDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
                    override fun onTaskLoaded(task: Task) {
                        callback.onTaskLoaded(task)
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    override fun saveTask(task: Task) {
        tasksRemoteDataSource.saveTask(task)
        tasksLocalDataSource.saveTask(task)

        cachedTasks.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        tasksRemoteDataSource.completeTask(task)
        tasksLocalDataSource.completeTask(task)

        val completedTask = Task(task.title, task.description, task.id, true)
        cachedTasks.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        val completedTask = getTaskWithId(taskId)
        if (completedTask != null) {
            completeTask(completedTask)
        }
    }

    override fun activateTask(task: Task) {
        tasksRemoteDataSource.activateTask(task)
        tasksLocalDataSource.activateTask(task)

        val activeTask = Task(task.title, task.description, task.id)
        cachedTasks.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        val activeTask = getTaskWithId(taskId)
        if (activeTask != null) {
            activateTask(activeTask)
        }
    }

    override fun clearCompletedTasks() {
        tasksRemoteDataSource.clearCompletedTasks()
        tasksLocalDataSource.clearCompletedTasks()

        val it = cachedTasks.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            if (entry.value.isCompleted) {
                it.remove()
            }
        }
    }

    override fun refreshTasks() {
        cacheIsDirty = true
    }

    override fun deleteAllTasks() {
        tasksRemoteDataSource.deleteAllTasks()
        tasksLocalDataSource.deleteAllTasks()

        cachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {
        tasksRemoteDataSource.deleteTask(taskId)
        tasksLocalDataSource.deleteTask(taskId)

        cachedTasks.remove(taskId)
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks.clear()
        for (task in tasks) {
            cachedTasks.put(task.id, task)
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.saveTask(task)
        }
    }

    private fun getTasksFromRemoteDataSource(callback: TasksDataSource.LoadTasksCallback) {
        tasksRemoteDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                refreshLocalDataSource(tasks)
                callback.onTasksLoaded(ArrayList(cachedTasks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun getTaskWithId(id: String) : Task? {
        if (cachedTasks.isEmpty()) {
            return null
        } else {
            return cachedTasks[id]
        }
    }
}