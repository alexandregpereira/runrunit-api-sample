package com.bano.runrunit.api

import com.bano.runrunit.model.Task
import retrofit2.Call
import retrofit2.http.*


/**
 * Interface to consume Task APIs
 */
interface TaskApi{
    @Headers(
        "Content-Type: application/json",
        "App-Key: daabf544adeda9936b42fcd5d85af91b",
        "User-Token: Nt94BX46JL3bg0BVqcKQ"
    )
    @GET("api/v1.0/tasks")
    fun listTask(@Query("responsible_id") user: String): Call<ArrayList<Task>>

    @Headers(
            "Content-Type: application/json",
            "App-Key: daabf544adeda9936b42fcd5d85af91b",
            "User-Token: Nt94BX46JL3bg0BVqcKQ"
    )
    @POST("api/v1.0/tasks/{idTask}/play")
    fun play(@Path("idTask") idTask: String): Call<Task>

    @Headers(
            "Content-Type: application/json",
            "App-Key: daabf544adeda9936b42fcd5d85af91b",
            "User-Token: Nt94BX46JL3bg0BVqcKQ"
    )
    @POST("api/v1.0/tasks/{idTask}/pause")
    fun pause(@Path("idTask") idTask: String): Call<Task>

    @Headers(
            "Content-Type: application/json",
            "App-Key: daabf544adeda9936b42fcd5d85af91b",
            "User-Token: Nt94BX46JL3bg0BVqcKQ"
    )
    @POST("api/v1.0/tasks/{idTask}/close")
    fun close(@Path("idTask") idTask: String): Call<Task>
}