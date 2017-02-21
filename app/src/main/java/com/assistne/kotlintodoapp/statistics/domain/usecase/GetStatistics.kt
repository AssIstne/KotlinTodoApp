package com.assistne.kotlintodoapp.statistics.domain.usecase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksDataSource
import com.assistne.kotlintodoapp.data.source.TasksRepository
import com.assistne.kotlintodoapp.statistics.domain.model.Statistics
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/21.
 */
class GetStatistics(val tasksRepository: TasksRepository) : UseCase<GetStatistics.RequestValues, GetStatistics.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                var activeTasks = 0
                var completedTasks = 0

                for ((title, description, id, isCompleted) in tasks) {
                    if (isCompleted) {
                        completedTasks += 1
                    } else {
                        activeTasks += 1
                    }
                }

                userCaseCallback?.onSuccess(ResponseValue(Statistics(completedTasks, activeTasks)))
            }

            override fun onDataNotAvailable() {
                userCaseCallback?.onError()
            }
        })
    }

    class RequestValues : UseCase.RequestValues

    class ResponseValue(val statistics: Statistics) : UseCase.ResponseValue
}