package com.assistne.kotlintodoapp.tasks.domain.usercase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksRepository

/**
 * Created by assistne on 17/2/20.
 */
class CompleteTask(val tasksRepository: TasksRepository) : UseCase<CompleteTask.RequestValues, CompleteTask.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        if (requestValues != null) {
            tasksRepository.completeTask(requestValues.completedTask)
            userCaseCallback?.onSuccess(ResponseValue())
        }
    }


    class RequestValues(val completedTask: String) : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}