package com.assistne.kotlintodoapp.statistics

import com.assistne.kotlintodoapp.BasePresenter
import com.assistne.kotlintodoapp.BaseView

/**
 * Created by assistne on 17/2/21.
 */
interface StatisticsContract {
    interface View : BaseView<Presenter> {
        fun setProgressIndicator(active: Boolean)
        fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int)
        fun showLoadingStatisticsError()
        fun isActive() : Boolean
    }

    interface Presenter : BasePresenter
}