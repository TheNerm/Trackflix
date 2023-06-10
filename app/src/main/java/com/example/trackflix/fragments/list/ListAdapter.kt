package com.example.trackflix.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackflix.R
import com.example.trackflix.database.Trackable
import com.example.trackflix.databinding.CustomRowBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var trackableList = emptyList<Trackable>()

    class MyViewHolder(val itemBinding: CustomRowBinding): RecyclerView.ViewHolder(itemBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return trackableList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = trackableList[position]
        holder.itemBinding.tbID.text =  currentItem.id.toString()
        holder.itemBinding.tbTitle.text = currentItem.title
        holder.itemBinding.tbType.text = currentItem.type

        val percentage = if (currentItem.currentProgress == 0){
            0
        }else{
            currentItem.goal/currentItem.currentProgress
        }
        holder.itemBinding.tbPercentage.text = "$percentage%"
    }

    fun setData(trackable: List<Trackable>){
        this.trackableList = trackable
        notifyDataSetChanged()
    }

}