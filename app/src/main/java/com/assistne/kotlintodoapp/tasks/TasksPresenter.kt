package com.assistne.kotlintodoapp.tasks

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.UseCaseHandler
import com.assistne.kotlintodoapp.tasks.domain.model.Task
import com.assistne.kotlintodoapp.tasks.domain.usercase.ActivateTask
import com.assistne.kotlintodoapp.tasks.domain.usercase.ClearCompleteTasks
import com.assistne.kotlintodoapp.tasks.domain.usercase.CompleteTask
import com.assistne.kotlintodoapp.tasks.domain.usercase.GetTasks

/**
 * Created by assistne on 17/2/17.
 */
class TasksPresenter(
        val useCaseHandler: UseCaseHandler,
        val tasksView: TasksContract.View,
        val getTasks: GetTasks,
        val completeTask: CompleteTask,
        val activateTask: ActivateTask,
        val clearCompleteTasks: ClearCompleteTasks) : TasksContract.Presenter {

    var currentFiltering = TasksFilterType.ALL_TASKS
    var firstLoad = true

    init {
        tasksView.setPresenter(this)
    }


    override fun start() {
        loadTasks(false)
    }

    override fun result(requestCode: Int, resultCode: Int) {
        if (true) {
            tasksView.showSuccessfullySavedMessage()
        }
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || firstLoad, true)
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            tasksView.setLoadingIndicator(true)
        }

        val requestValue = GetTasks.RequestValues(forceUpdate, currentFiltering)

        useCaseHandler.execute(getTasks, requestValue, object : UseCase.UseCaseCallback<GetTasks.ResponseValue> {
            override fun onSuccess(response: GetTasks.ResponseValue) {
                val tasks = response.tasks
                if (!tasksView.isActive()) {
                    return
                }
                if (showLoadingUI) {
                    tasksView.setLoadingIndicator(false)
                }

                processTasks(tasks)
            }

            override fun onError() {
                if (!tasksView.isActive()) {
                    return
                }
                tasksView.showLoadingTasksError()
            }
        })
    }

    private fun processTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            processEmptyTasks()
        } else {
            tasksView.showTasks(tasks)
            showFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showNoCompletedTasks()
            else -> tasksView.showNoTasks()
        }
    }

    private fun showFilterLabel() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showCompletedFilterLabel()
            else -> tasksView.showAllFilterLabel()
        }
    }

    override fun addNewTask() {
        tasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        tasksView.showTaskDetailsUi(requestedTask.id)
    }

    override fun completeTask(completedTask: Task) {
        useCaseHandler.execute(completeTask, CompleteTask.RequestValues(completedTask.id),
                object : UseCase.UseCaseCallback<CompleteTask.ResponseValue>{
                    override fun onSuccess(response: CompleteTask.ResponseValue) {
                        tasksView.showTaskMarkedComplete()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        tasksView.showLoadingTasksError()
                    }
                })
    }

    override fun activateTask(activeTask: Task) {
        useCaseHandler.execute(activateTask, ActivateTask.RequestValues(activeTask.id),
                object : UseCase.UseCaseCallback<ActivateTask.ResponseValue> {
                    override fun onSuccess(response: ActivateTask.ResponseValue) {
                        tasksView.showTaskMarkedActive()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        tasksView.showLoadingTasksError()
                    }
                })
    }

    override fun clearCompletedTasks() {
        useCaseHandler.execute(clearCompleteTasks, ClearCompleteTasks.RequestValues(),
                object : UseCase.UseCaseCallback<ClearCompleteTasks.ResponseValue> {
                    override fun onSuccess(response: ClearCompleteTasks.ResponseValue) {
                        tasksView.showCompletedTasksCleared()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        tasksView.showLoadingTasksError()
                    }
                })
    }

    override fun setFiltering(requestType: TasksFilterType) {
        currentFiltering = requestType
    }

    override fun getFiltering(): TasksFilterType = currentFiltering
}