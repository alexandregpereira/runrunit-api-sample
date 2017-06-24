package com.bano.runrunit.model

import com.google.gson.annotations.SerializedName

/**
 * Runrun.it task
 */
class Task{
    var id: String = ""
    var title: String = ""
    @SerializedName("is_working_on")
    var workingOn: Boolean = false
    @SerializedName("time_progress")
    var timeProgress: Double = 0.0
}