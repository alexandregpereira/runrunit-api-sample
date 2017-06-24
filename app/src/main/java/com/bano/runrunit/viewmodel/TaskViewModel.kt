package com.bano.runrunit.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bano.runrunit.model.Task
import com.bano.runrunit.repository.TaskRepository

/**
 * View Model that represents the Task model
 */
class TaskViewModel: ViewModel() {
    val taskObservable: MutableLiveData<List<Task>> = MutableLiveData()
    private val taskRepository = TaskRepository()

    fun getTasks(): LiveData<List<Task>>{
        taskRepository.listTask{
            taskObservable.value = it
        }
        return taskObservable
    }
}