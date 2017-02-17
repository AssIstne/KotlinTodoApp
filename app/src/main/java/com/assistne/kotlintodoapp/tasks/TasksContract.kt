package com.assistne.kotlintodoapp.tasks

import com.assistne.kotlintodoapp.BasePresenter
import com.assistne.kotlintodoapp.BaseView
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/17.
 */
interface TasksContract {
    interface View : BaseView<Presenter> {
        fun setLoadingIndicator(active: Boolean)
        fun showTasks(tasks: List<Task>)
        fun showAddTask()
        fun showTaskDetailsUi(taskId: String)
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
        fun showCompletedTasksCleared()
        fun showLoadingTasksError()
        fun showNotTasks()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showSuccessfullySavedMessage()
        fun isActive() : Boolean
        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {
        fun result(requestCode: Int, resultCode: Int)
        fun loadTasks(forceUpdate: Boolean)
        fun addNewTask()
        fun openTaskDetails(requestedTask: Task)
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
        fun clearCompletedTasks()
        fun setFiltering(requestType: TasksFilterType)
        fun getFiltering() : TasksFilterType
    }
}