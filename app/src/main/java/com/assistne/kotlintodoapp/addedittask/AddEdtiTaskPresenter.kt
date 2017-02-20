package com.assistne.kotlintodoapp.addedittask

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.UseCaseHandler
import com.assistne.kotlintodoapp.addedittask.domain.usecase.GetTask
import com.assistne.kotlintodoapp.addedittask.domain.usecase.SaveTask
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/20.
 */
class AddEdtiTaskPresenter(
        val useCaseHandler: UseCaseHandler,
        val taskId: String?,
        val addTaskView: AddEditTaskContract.View,
        val getTask: GetTask,
        val saveTask: SaveTask) : AddEditTaskContract.Presenter {

    init {
        addTaskView.setPresenter(this)
    }

    override fun saveTask(title: String?, description: String?) {
        if (isNewTask()) {
            createTask(title, description)
        } else {
            updateTask(title, description)
        }
    }

    private fun updateTask(title: String?, description: String?) {
        if (taskId == null) {
            throw RuntimeException("updateTask() was called but task is new.")
        }

        val newTask = Task(title, description, taskId)
        useCaseHandler.execute(saveTask, SaveTask.RequestValues(newTask), object : UseCase.UseCaseCallback<SaveTask.ResponseValue>{
            override fun onSuccess(response: SaveTask.ResponseValue) {
                addTaskView.showTasksList()
            }

            override fun onError() {
                showSaveError()
            }
        })
    }

    private fun createTask(title: String?, description: String?) {
        val newTask = Task(title, description)
        if (newTask.isEmpty()) {
            addTaskView.showEmptyTaskError()
        } else {
            useCaseHandler.execute(saveTask, SaveTask.RequestValues(newTask), object : UseCase.UseCaseCallback<SaveTask.ResponseValue>{
                override fun onSuccess(response: SaveTask.ResponseValue) {
                    addTaskView.showTasksList()
                }

                override fun onError() {
                    showSaveError()
                }
            })
        }
    }

    private fun isNewTask(): Boolean {
        return taskId == null
    }

    override fun populateTask() {
        if (taskId == null) {
            throw RuntimeException("populateTask() was called but task is new.")
        }

        useCaseHandler.execute(getTask, GetTask.RequestValues(taskId), object : UseCase.UseCaseCallback<GetTask.ResponseValue> {
            override fun onSuccess(response: GetTask.ResponseValue) {
                showTask(response.task)
            }

            override fun onError() {
                showEmptyTaskError()
            }
        })
    }

    private fun showSaveError() {
        // Show error, log, etc.
    }

    private fun showEmptyTaskError() {
        if (addTaskView.isActive()) {
            addTaskView.showEmptyTaskError()
        }
    }

    private fun showTask(task: Task) {
        if (addTaskView.isActive()) {
            addTaskView.setTitle(task.title)
            addTaskView.setDescription(task.description)
        }
    }

    override fun start() {
        if (taskId != null) {
            populateTask()
        }
    }
}