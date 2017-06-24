package com.bano.runrunit.activity

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bano.runrunit.R
import com.bano.runrunit.adapter.TaskAdapter
import com.bano.runrunit.databinding.ActivityMainBinding
import com.bano.runrunit.model.Task
import com.bano.runrunit.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.toolbar.setTitle(R.string.app_name)

        binding.contentMain.recyclerTask.layoutManager = LinearLayoutManager(this)
        val model = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        model.getTasks().observe(this, Observer<List<Task>> {
            it?.let { binding.contentMain.recyclerTask.adapter = TaskAdapter(this@MainActivity, it, null) }
        })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }
}
