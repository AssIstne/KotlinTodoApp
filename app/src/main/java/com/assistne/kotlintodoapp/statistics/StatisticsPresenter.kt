package com.assistne.kotlintodoapp.statistics

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.UseCaseHandler
import com.assistne.kotlintodoapp.statistics.domain.usecase.GetStatistics

/**
 * Created by assistne on 17/2/21.
 */
class StatisticsPresenter(
        val useCaseHandler: UseCaseHandler,
        val statisticsView: StatisticsContract.View,
        val getStatistics: GetStatistics) : StatisticsContract.Presenter {

    init {
        statisticsView.setPresenter(this)
    }

    override fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        statisticsView.setProgressIndicator(true)

        useCaseHandler.execute(getStatistics, GetStatistics.RequestValues(),
                object : UseCase.UseCaseCallback<GetStatistics.ResponseValue> {
                    override fun onSuccess(response: GetStatistics.ResponseValue) {
                        if (statisticsView.isActive()) {
                            statisticsView.setProgressIndicator(false)
                            val statistics = response.statistics
                            statisticsView.showStatistics(statistics.activeTasks, statistics.completedTasks)
                        }
                    }

                    override fun onError() {
                        if (statisticsView.isActive()) {
                            statisticsView.showLoadingStatisticsError()
                        }
                    }
                })
    }
}