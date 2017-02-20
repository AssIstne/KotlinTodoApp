package com.assistne.kotlintodoapp.addedittask.domain.usecase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksDataSource
import com.assistne.kotlintodoapp.data.source.TasksRepository
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/20.
 */
class GetTask(val tasksRepository: TasksRepository) : UseCase<GetTask.RequestValues, GetTask.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        if (requestValues != null) {
            tasksRepository.getTask(requestValues.taskId, object : TasksDataSource.GetTaskCallback{
                override fun onTaskLoaded(task: Task) {
                    userCaseCallback?.onSuccess(ResponseValue(task))
                }

                override fun onDataNotAvailable() {
                    userCaseCallback?.onError()
                }
            })
        }
    }

    class RequestValues(val taskId: String) : UseCase.RequestValues

    class ResponseValue(val task: Task) : UseCase.ResponseValue
}