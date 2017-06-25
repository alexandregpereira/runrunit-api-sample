package com.bano.runrunit.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.bano.runrunit.api.TaskApi
import com.bano.runrunit.model.Task
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * View Model that represents the Task model
 */
class TaskViewModel: ViewModel() {
    val errorObservable: MutableLiveData<Unit> = MutableLiveData()
    private val taskApi: TaskApi
    private val realm = Realm.getDefaultInstance()
    val taskListObservable: MutableLiveData<List<Task>> = MutableLiveData()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://secure.runrun.it/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        taskApi = retrofit.create<TaskApi>(TaskApi::class.java)
    }

    fun getTasks(): LiveData<List<Task>>{
        return listTask()
    }

    private fun listTask(): LiveData<List<Task>>{
        taskListObservable.value = realm.where(Task::class.java).findAll().sort("queuePosition")

        listTaskFomApi()

        return taskListObservable
    }

    private fun listTaskFomApi(){
        taskApi.listTask("alexandre-gomes-pereira").enqueue(object : Callback<ArrayList<Task>> {
            override fun onFailure(call: Call<ArrayList<Task>>?, t: Throwable?) {
                Log.e("Erro retrofit", t?.message)
                errorObservable.value = Unit
            }

            override fun onResponse(call: Call<ArrayList<Task>>, response: Response<ArrayList<Task>>) {
                Log.d("Recebido Retrofit", response.body().toString())
                insertOrUpdate(response.body())
                taskListObservable.value = response.body()
            }
        })
    }

    private fun insertOrUpdate(tasks : ArrayList<Task>?){
        if(tasks == null) return
        realm.executeTransactionAsync { it.insertOrUpdate(tasks) }
    }

    private fun insertOrUpdate(task: Task){
        realm.executeTransactionAsync { it.insertOrUpdate(task) }
    }

    private fun delete(task: Task){
        realm.executeTransactionAsync { it.where(Task::class.java).equalTo("id", task.id).findAll().deleteAllFromRealm() }
    }

    val taskCallback = object : Callback<Task> {
        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            Log.e("Erro retrofit", t?.message)
            errorObservable.value = Unit
        }

        override fun onResponse(call: Call<Task>, response: Response<Task>) {
            Log.d("Recebido Retrofit", response.body().toString())
            listTaskFomApi()
        }
    }

    fun play(task: Task) {
        if(task.workingOn) return
        task.workingOn = true
        insertOrUpdate(task)
        taskApi.play(task.id).enqueue(taskCallback)
    }

    fun pause(task: Task) {
        if(!task.workingOn) return
        task.workingOn = false
        insertOrUpdate(task)
        taskApi.pause(task.id).enqueue(taskCallback)
    }

    fun close(task: Task) {
        delete(task)
        taskApi.close(task.id).enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                Log.e("Erro retrofit", t?.message)
                errorObservable.value = Unit
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                Log.d("Recebido Retrofit", response.body().toString())
            }
        })
    }
}