package com.assistne.kotlintodoapp.tasks.domain.usercase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksRepository

/**
 * Created by assistne on 17/2/20.
 */
class ActivateTask(private val tasksRepository: TasksRepository) : UseCase<ActivateTask.RequestValues, ActivateTask.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        if (requestValues != null) {
            tasksRepository.activateTask(requestValues.activateTask)
            userCaseCallback?.onSuccess(ResponseValue())
        }
    }

    class RequestValues(val activateTask: String) : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}