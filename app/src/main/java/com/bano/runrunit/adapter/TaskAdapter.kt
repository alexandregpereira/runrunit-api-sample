package com.bano.runrunit.adapter

import android.content.Context
import android.os.Build
import android.view.View
import com.bano.goblin.adapter.BaseBottomSpaceAdapter
import com.bano.runrunit.R
import com.bano.runrunit.databinding.ItemTaskBinding
import com.bano.runrunit.model.Task

class TaskAdapter(context: Context, items: List<Task>, listener: ((t: Task) -> Unit)?) :
        BaseBottomSpaceAdapter<Task, ItemTaskBinding>(context, items, R.layout.item_task, listener) {

    override fun onBindViewHolder(e: ItemTaskBinding?, t: Task?) {
        if(e == null || t == null) return
        with(e) {
            with(t) {
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