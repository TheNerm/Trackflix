package com.example.trackflix.statistics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.trackflix.R
import com.example.trackflix.database.TrackableProgressionState
import com.example.trackflix.database.TrackableType
import com.example.trackflix.databinding.FragmentStatisticsBinding
import com.example.trackflix.fragments.random.RandomFragmentArgs
import com.example.trackflix.model.Trackable

/**
 * The Fragment holding the information about the Statistics
 */
class Statistics : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private val args by navArgs<StatisticsArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        var hWatched = 0
        var pRead = 0
        var hPlayed = 0
        var hNotWatched = 0
        var pNotRead = 0
        var hNotPlayed = 0
        var nBacklog = 0
        var nInProgress = 0
        var nFinished = 0
        var nCancelled = 0
        var nUnreleased = 0


        args.trackableList.trackables.forEach { trackable->
            when(trackable.type){
                "Book" -> {
                    pRead += trackable.currentProgress
                    pNotRead += trackable.goal - trackable.currentProgress
                }
                "Movie" -> {
                    hWatched += trackable.currentProgress
                    hNotWatched += trackable.goal - trackable.currentProgress
                }
                "Series" -> {
                    hWatched += trackable.currentProgress
                    hNotWatched += trackable.goal - trackable.currentProgress
                }
                "Game" -> {
                    hPlayed += trackable.currentProgress
                    hNotPlayed += trackable.goal - trackable.currentProgress
                }
            }
            when(trackable.progressState){
                "backlog" -> nBacklog++
                "inProgress" -> nInProgress++
                "finished" -> nFinished++
                "cancelled" -> nCancelled++
            }
            if(trackable.releaseDate.isNotEmpty()){
                nUnreleased++
            }
        }

        binding.consumedTVS.text = hWatched.toString()
        binding.pagesConsumedTVS.text = pRead.toString()
        binding.consPlayedTVS.text = hPlayed.toString()
        binding.hours2bWatchedTVS.text = hNotWatched.toString()
        binding.pages2bReadTVS.text = pNotRead.toString()
        binding.hours2bPlayedTVS.text = hPlayed.toString()
        binding.backlogItemsTVS.text = nBacklog.toString()
        binding.progressItemsTVS.text = nInProgress.toString()
        binding.finishedItemsTVS.text = nFinished.toString()
        binding.cancelledItemsTVS.text = nCancelled.toString()
        binding.unreleasedItemsTVS.text = nUnreleased.toString()

        return binding.root
    }
}