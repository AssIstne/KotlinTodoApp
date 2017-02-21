package com.assistne.kotlintodoapp.taskdetail

import com.assistne.kotlintodoapp.BasePresenter
import com.assistne.kotlintodoapp.BaseView

/**
 * Created by assistne on 17/2/21.
 */
interface TaskDetailContract {
    interface View : BaseView<Presenter> {
        fun setLoadingIndicator(active: Boolean)
        fun showMissingTask()
        fun hideTitle()
        fun showTitle(title: String?)
        fun hideDescription()
        fun showDescription(description: String?)
        fun showCompletionStatus(complete: Boolean)
        fun showEditTask(taskId: String)
        fun showTaskDeleted()
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
        fun isActive() : Boolean
    }
    interface Presenter : BasePresenter {
        fun editTask()
        fun deleteTask()
        fun completeTask()
        fun activateTask()
    }
}