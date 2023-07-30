package com.example.trackflix.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.GestureDetectorCompat
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
    lateinit var progressUpdater: CustomProgressBar.ProgressUpdater

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
        updateTrackableVisuals(holder, currentItem)

        val trackables = TrackableList(trackableList)

        val custom = GestureDetectorCompat(holder.itemView.context, holder.itemBinding.progressBar)
        holder.itemBinding.progressBar.mDetector = custom
        holder.itemBinding.progressBar.trackable = currentItem
        holder.itemBinding.progressBar.holder = holder
        holder.itemBinding.progressBar.updateTrackableVisuals = ::updateTrackableVisuals
        holder.itemBinding.progressBar.progressUpdater = progressUpdater

        holder.itemBinding.rowElement.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(trackables, position)
            Navigation.findNavController(holder.itemBinding.root).navigate(action)
        }
        //need to set it up this way because of the gesture detection
        holder.itemBinding.progressBar.setOnClickListener {
            holder.itemBinding.rowElement.performClick()
        }
    }

    private fun updateTrackableVisuals(holder: MyViewHolder, currentItem: Trackable){
        holder.itemBinding.tbTitle.text = currentItem.title
        holder.itemBinding.priorityRB.rating = (currentItem.prio)

        val unit = when(currentItem.type){
            TrackableType.BOOK.value -> "p"
            else -> "h"
        }
        val absoluteProgressString = holder.itemView.context.getString(R.string.absoluteProgress, currentItem.currentProgress, currentItem.goal, unit)
        val percentage = calculatePercentage(currentItem.currentProgress, currentItem.goal)
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
            "finished"-> R.color.teal_700
            "cancelled"-> R.color.card_cancelled
            else -> {
                R.color.card_default}
        }
        holder.itemBinding.cardView.setCardBackgroundColor( getColor(holder.itemBinding.cardView.context, cardBgColorResource))

        //type symbol
        val typeDrawable = when(currentItem.type){
            TrackableType.BOOK.value -> holder.itemView.getResources().getDrawable(R.drawable.book)
            TrackableType.MOVIE.value -> holder.itemView.getResources().getDrawable(R.drawable.movie)
            TrackableType.SERIES.value -> holder.itemView.getResources().getDrawable(R.drawable.series)
            TrackableType.GAME.value -> holder.itemView.getResources().getDrawable(R.drawable.game)
            else -> holder.itemView.getResources().getDrawable(R.drawable.movie)
        }
        holder.itemBinding.typeSymbol.setImageDrawable(typeDrawable)
    }

    private fun calculatePercentage(currentProgress: Int, goal: Int): Int{
        return if (currentProgress == 0){
            0
        }else{
            (100*(currentProgress.toFloat()/goal.toFloat())).toInt()
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