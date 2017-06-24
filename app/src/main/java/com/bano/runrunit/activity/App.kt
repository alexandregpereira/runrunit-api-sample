package com.bano.runrunit.activity

import android.app.Application
import io.realm.Realm

/**
 *
 * Created by Alexandre on 24/06/2017.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}