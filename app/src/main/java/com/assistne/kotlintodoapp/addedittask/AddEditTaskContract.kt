package com.assistne.kotlintodoapp.addedittask

import com.assistne.kotlintodoapp.BasePresenter
import com.assistne.kotlintodoapp.BaseView

/**
 * Created by assistne on 17/2/20.
 */
interface AddEditTaskContract {
    interface View : BaseView<Presenter> {
        fun showEmptyTaskError()
        fun showTasksList()
        fun setTitle(title: String?)
        fun setDescription(description: String?)
        fun isActive() : Boolean
    }

    interface Presenter : BasePresenter {
        fun saveTask(title: String?, description: String?)
        fun populateTask()
    }
}