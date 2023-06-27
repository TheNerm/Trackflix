package com.example.trackflix.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.trackflix.databinding.CustomRowBinding
import com.example.trackflix.model.TrackableList
import com.example.trackflix.model.Trackable

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
        holder.itemBinding.tbTitle.text = currentItem.title

        val trackables = TrackableList(trackableList)

        holder.itemBinding.tbPriority.text = (currentItem.prio * 2).toInt().toString()
        val percentage = if (currentItem.currentProgress == 0){
            0
        }else{
            (100*(currentItem.currentProgress.toFloat()/currentItem.goal.toFloat())).toInt()
        }
        //primary progress bar
        holder.itemBinding.tbPercentage.text = "$percentage%"
        holder.itemBinding.progressBar.progress=percentage
        //secondary progress bar
        val secondProgress = percentage -100
        holder.itemBinding.progressBar.secondaryProgress= secondProgress.coerceAtLeast(0)

        holder.itemBinding.rowElement.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(trackables, position)
            Navigation.findNavController(holder.itemBinding.root).navigate(action)
        }

    }

    fun setData(trackable: List<Trackable>){
        this.trackableList = trackable
        notifyDataSetChanged()
    }

}