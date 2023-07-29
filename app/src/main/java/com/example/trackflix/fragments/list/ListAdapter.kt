package com.example.trackflix.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.trackflix.R
import com.example.trackflix.database.TrackableType
import com.example.trackflix.databinding.CustomRowBinding
import com.example.trackflix.fragments.update.UpdateFragment
import com.example.trackflix.model.Trackable
import com.example.trackflix.model.TrackableList
import java.util.Calendar


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

        holder.itemBinding.priorityRB.rating = (currentItem.prio)
        val unit = when(currentItem.type){
            TrackableType.BOOK.value -> "p"
            else -> "h"
        }
        val absoluteProgressString = holder.itemView.context.getString(R.string.absoluteProgress, currentItem.currentProgress, currentItem.goal, unit)
        val percentage = if (currentItem.currentProgress == 0){
            0
        }else{
            (100*(currentItem.currentProgress.toFloat()/currentItem.goal.toFloat())).toInt()
        }

        val reldate: String = currentItem.releaseDate
        if(isTrackableUnreleased(reldate)){
            holder.itemBinding.progress.text = reldate.replace('-','.')
        }else{
            holder.itemBinding.progress.text = absoluteProgressString
            holder.itemBinding.progress.setOnClickListener {
                if(holder.itemBinding.progress.text.endsWith("%")){
                    holder.itemBinding.progress.text = absoluteProgressString
                }else{
                    holder.itemBinding.progress.text = holder.itemView.context.getString(R.string.percentageProgress, percentage)
                }

            }
        }

        //primary progress bar
        holder.itemBinding.progressBar.progress=percentage
        //secondary progress bar
        val secondProgress = percentage -100
        holder.itemBinding.progressBar.secondaryProgress= secondProgress.coerceAtLeast(0)

        val cardBgColorResource = when(currentItem.progressState){
            "backlog"-> R.color.card_default
            "inProgress"-> R.color.card_default
            "finished"-> R.color.card_finished
            "cancelled"-> R.color.card_cancelled
            else -> {
                R.color.card_default}
        }
        holder.itemBinding.cardView.setCardBackgroundColor( getColor(holder.itemBinding.cardView.context, cardBgColorResource))

        val typeDrawable = when(currentItem.type){
            TrackableType.BOOK.value -> holder.itemView.getResources().getDrawable(R.drawable.book)
            TrackableType.MOVIE.value -> holder.itemView.getResources().getDrawable(R.drawable.movie)
            TrackableType.SERIES.value -> holder.itemView.getResources().getDrawable(R.drawable.series)
            TrackableType.GAME.value -> holder.itemView.getResources().getDrawable(R.drawable.game)
            else -> holder.itemView.getResources().getDrawable(R.drawable.movie)
        }
        holder.itemBinding.typeSymbol.setImageDrawable(typeDrawable)
        holder.itemBinding.rowElement.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(trackables, position)
            Navigation.findNavController(holder.itemBinding.root).navigate(action)
        }

    }

    private fun isTrackableUnreleased(reldate: String):Boolean{
        if(reldate != "") {
            val dateFormat = "dd-MM-yyyy"
            val releaseDateCalendar = UpdateFragment.stringToCalendar(reldate, dateFormat)
            if (releaseDateCalendar != null && releaseDateCalendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                return true
            }
        }
        return false
    }

    fun setData(trackable: List<Trackable>){
        this.trackableList = trackable
        notifyDataSetChanged()
    }

}