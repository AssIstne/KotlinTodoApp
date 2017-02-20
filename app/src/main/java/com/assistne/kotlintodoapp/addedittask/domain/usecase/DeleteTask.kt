package com.assistne.kotlintodoapp.addedittask.domain.usecase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksRepository

/**
 * Created by assistne on 17/2/20.
 */
class DeleteTask(val tasksRepository: TasksRepository) : UseCase<DeleteTask.RequestValues, DeleteTask.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        if (requestValues != null) {
            tasksRepository.deleteTask(requestValues.taskId)
            userCaseCallback?.onSuccess(ResponseValue())
        }
    }

    class RequestValues(val taskId: String) : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue

}