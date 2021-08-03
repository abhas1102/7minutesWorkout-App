package com.example.dailyworkoutfor7minutes

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_exercise_status.view.*

class ExercriseStatusAdapter(val items:ArrayList<ExerciseModel>,val context:Context) :RecyclerView.Adapter<ExercriseStatusAdapter.ViewHolder>(){
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){ // We want to describe the item View from this viewHolder
        val tvItem = view.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // It's called when the view holder is created but it's inflate the item view xml layout
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise_status,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // Assign the right text to every single one of those circles
        val model:ExerciseModel = items[position]
        holder.tvItem.text = model.getId().toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}