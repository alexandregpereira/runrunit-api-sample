package com.bano.runrunit.adapter

import android.content.Context
import android.os.Build
import android.view.View
import com.bano.goblin.adapter.BaseBottomSpaceAdapter
import com.bano.runrunit.R
import com.bano.runrunit.activity.MainActivity
import com.bano.runrunit.databinding.ItemTaskBinding
import com.bano.runrunit.model.Task

class TaskAdapter(context: Context, items: List<Task>, val taskListener: MainActivity.TaskListener) :
        BaseBottomSpaceAdapter<Task, ItemTaskBinding>(context, items, R.layout.item_task, null) {

    override fun onBindViewHolder(e: ItemTaskBinding?, t: Task?) {
        if(e == null || t == null) return
        with(e) {
            with(t) {
                imgPlay.tag = t
                imgPause.tag = t
                btnDelivery.tag = t

                imgPlay.setOnClickListener {
                    val task = it.tag as Task
                    taskListener.onPlay(task)
                    setItem(task)
                }
                imgPause.setOnClickListener {
                    val task = it.tag as Task
                    taskListener.onPause(task)
                    setItem(task)
                }
                btnDelivery.setOnClickListener {
                    val task = it.tag as Task
                    taskListener.onClose(task)
                    remove(task)
                }

                txtTitle.text = "$id - $title"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(timeProgress.toInt(), true)
                }
                else progressBar.progress = timeProgress.toInt()

                if(workingOn){
                    imgPause.visibility = View.VISIBLE
                    imgPlay.visibility = View.INVISIBLE
                }
                else{
                    imgPause.visibility = View.INVISIBLE
                    imgPlay.visibility = View.VISIBLE
                }
            }
        }
    }
}