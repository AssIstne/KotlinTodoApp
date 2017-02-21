package com.assistne.kotlintodoapp.taskdetail

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.UseCaseHandler
import com.assistne.kotlintodoapp.addedittask.domain.usecase.DeleteTask
import com.assistne.kotlintodoapp.addedittask.domain.usecase.GetTask
import com.assistne.kotlintodoapp.tasks.domain.model.Task
import com.assistne.kotlintodoapp.tasks.domain.usercase.ActivateTask
import com.assistne.kotlintodoapp.tasks.domain.usercase.CompleteTask

/**
 * Created by assistne on 17/2/21.
 */
class TaskDetailPresenter(
        val useCaseHandler: UseCaseHandler,
        val taskId: String?,
        val taskDetailView: TaskDetailContract.View,
        val getTask: GetTask,
        val completeTask: CompleteTask,
        val activateTask: ActivateTask,
        val deleteTask: DeleteTask) : TaskDetailContract.Presenter {


    init {
        taskDetailView.setPresenter(this)
    }

    override fun editTask() {
        if (taskId.isNullOrEmpty()) {
            taskDetailView.showMissingTask()
        } else {
            taskDetailView.showEditTask(taskId as String)
        }
    }

    override fun deleteTask() {
        useCaseHandler.execute(deleteTask, DeleteTask.RequestValues(taskId as String),
                object : UseCase.UseCaseCallback<DeleteTask.ResponseValue> {
                    override fun onSuccess(response: DeleteTask.ResponseValue) {
                        taskDetailView.showTaskDeleted()
                    }

                    override fun onError() {

                    }
                })
    }

    override fun completeTask() {
        if (taskId.isNullOrEmpty()) {
            taskDetailView.showMissingTask()
            return
        }

        useCaseHandler.execute(completeTask, CompleteTask.RequestValues(taskId as String),
                object : UseCase.UseCaseCallback<CompleteTask.ResponseValue> {
                    override fun onSuccess(response: CompleteTask.ResponseValue) {
                        taskDetailView.showTaskMarkedComplete()
                    }

                    override fun onError() {

                    }
                })
    }

    override fun activateTask() {
        if (taskId.isNullOrEmpty()) {
            taskDetailView.showMissingTask()
            return
        }

        useCaseHandler.execute(activateTask, ActivateTask.RequestValues(taskId as String),
                object : UseCase.UseCaseCallback<ActivateTask.ResponseValue> {
                    override fun onSuccess(response: ActivateTask.ResponseValue) {
                        taskDetailView.showTaskMarkedActive()
                    }

                    override fun onError() {

                    }
                })
    }

    override fun start() {
        openTask()
    }

    private fun openTask() {
        if (taskId.isNullOrEmpty()) {
            taskDetailView.showMissingTask()
            return
        }

        taskDetailView.setLoadingIndicator(true)

        useCaseHandler.execute(getTask, GetTask.RequestValues(taskId as String),
                object : UseCase.UseCaseCallback<GetTask.ResponseValue> {
                    override fun onSuccess(response: GetTask.ResponseValue) {
                        val task = response.task
                        if (taskDetailView.isActive()) {
                            taskDetailView.setLoadingIndicator(false)
                            showTask(task)
                        }
                    }

                    override fun onError() {
                        if (taskDetailView.isActive()) {
                            taskDetailView.showMissingTask()
                        }
                    }
                })
    }

    private fun showTask(task: Task) {
        val title = task.title
        val description = task.description
        if (title.isNullOrEmpty()) {
            taskDetailView.hideTitle()
        } else {
            taskDetailView.showTitle(title)
        }

        if (description.isNullOrEmpty()) {
            taskDetailView.hideDescription()
        } else {
            taskDetailView.showDescription(description)
        }
        taskDetailView.showCompletionStatus(task.isCompleted)
    }
}