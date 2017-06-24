package com.bano.runrunit.repository

import com.bano.runrunit.api.TaskApi
import com.bano.runrunit.model.Task
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


    fun listTask(finish : (List<Task>?) -> Unit){


        taskApi.listTask("alexandre-gomes-pereira").enqueue(object : Callback<ArrayList<Task>> {
            override fun onFailure(call: Call<ArrayList<Task>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ArrayList<Task>>, response: Response<ArrayList<Task>>) {
                finish(response.body())
            }
        })
    }

}