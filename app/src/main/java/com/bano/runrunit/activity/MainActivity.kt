package com.bano.runrunit.activity

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.bano.runrunit.R
import com.bano.runrunit.adapter.TaskAdapter
import com.bano.runrunit.databinding.ActivityMainBinding
import com.bano.runrunit.model.Task
import com.bano.runrunit.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    interface TaskListener{
        fun onPlay(task: Task)
        fun onPause(task: Task)
        fun onClose(task: Task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.toolbar.setTitle(R.string.app_name)
        val model = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        binding.contentMain.recyclerTask.layoutManager = LinearLayoutManager(this)
        val adapter = TaskAdapter(this@MainActivity, ArrayList<Task>(), object: TaskListener{
            override fun onPlay(task: Task) {
                model.play(task)
            }

            override fun onPause(task: Task) {
                model.pause(task)
            }

            override fun onClose(task: Task) {
                model.close(task)
            }
        })
        binding.contentMain.recyclerTask.adapter = adapter

        val taskObservable = model.getTasks()
        taskObservable.observe(this, Observer<List<Task>> {
            it?.let {
                Log.d("Recebido MainActivity", it.toString())
                adapter.items = it
            }
        })

        model.errorObservable.observe(this, Observer<Unit> {
            Log.e("Recebido MainActivity", "Erro")
            val taskList: List<Task>? = taskObservable.value
            if(taskList == null || taskList.isEmpty()) showEmptyMessage(true, binding)
        })

        binding.contentMain.btnTryAgain.setOnClickListener {
            showEmptyMessage(false, binding)
            model.getTasks()
        }
    }

    private fun showEmptyMessage(show: Boolean, binding: ActivityMainBinding){
        if(show){
            binding.contentMain.txtEmpty.visibility = View.VISIBLE
            binding.contentMain.btnTryAgain.visibility = View.VISIBLE
        }
        else{
            binding.contentMain.txtEmpty.visibility = View.GONE
            binding.contentMain.btnTryAgain.visibility = View.GONE
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }
}
