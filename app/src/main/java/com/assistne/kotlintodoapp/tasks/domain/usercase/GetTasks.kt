package com.assistne.kotlintodoapp.tasks.domain.usercase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.tasks.TasksFilterType
import com.assistne.kotlintodoapp.data.source.TasksDataSource
import com.assistne.kotlintodoapp.data.source.TasksRepository
import com.assistne.kotlintodoapp.tasks.domain.filter.FilterFactory
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/20.
 */
class GetTasks(private val tasksRepository: TasksRepository, private val filterFactory: FilterFactory) : UseCase<GetTasks.RequestValues, GetTasks.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        if (requestValues != null) {
            if (requestValues.isForceUpdate) {
                tasksRepository.refreshTasks()
            }
            tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {
                    val currentFiltering = requestValues.currentFiltering
                    val taskFilter = filterFactory.create(currentFiltering)

                    if (taskFilter != null) {
                        val tasksFiltered = taskFilter.filter(tasks)
                        val responseValue = ResponseValue(tasksFiltered)
                        userCaseCallback?.onSuccess(responseValue)
                    }
                }

                override fun onDataNotAvailable() {
                    userCaseCallback?.onError()
                }
            })
        }
    }

    class RequestValues(val isForceUpdate: Boolean, val currentFiltering: TasksFilterType) : UseCase.RequestValues

    class ResponseValue(val tasks: List<Task>) : UseCase.ResponseValue
}