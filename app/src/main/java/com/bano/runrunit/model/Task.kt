package com.bano.runrunit.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Runrun.it task
 */
open class Task : RealmObject(){
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    @SerializedName("is_working_on")
    var workingOn: Boolean = false
    @SerializedName("time_progress")
    var timeProgress: Double = 0.0
}