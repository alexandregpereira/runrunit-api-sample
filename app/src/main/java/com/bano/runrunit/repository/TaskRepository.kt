package com.bano.runrunit.repository

import com.bano.runrunit.api.TaskApi
import com.bano.runrunit.model.Task
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Repository that represents Task model
 */
class TaskRepository {
    private val taskApi: TaskApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://secure.runrun.it/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        taskApi = retrofit.create<TaskApi>(TaskApi::class.java)
    }

    private fun insertOrUpdate(tasks : ArrayList<Task>?){
        if(tasks == null) return
        Realm.getDefaultInstance().use { it.executeTransaction { it.insertOrUpdate(tasks) } }
    }

    fun listTask(finish : (List<Task>?) -> Unit){
        listTaskOnDb(finish)
        taskApi.listTask("alexandre-gomes-pereira").enqueue(object : Callback<ArrayList<Task>> {
            override fun onFailure(call: Call<ArrayList<Task>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ArrayList<Task>>, response: Response<ArrayList<Task>>) {
                insertOrUpdate(response.body())
                listTaskOnDb(finish)
            }
        })
    }

    private fun listTaskOnDb(finish : (List<Task>?) -> Unit){
        val realm = Realm.getDefaultInstance()
        realm.where(Task::class.java).findAllAsync().addChangeListener(RealmChangeListener<RealmResults<Task>> {
            finish(it)
        })
    }

}