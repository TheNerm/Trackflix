package com.example.trackflix.fragments.random

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.trackflix.R
import com.example.trackflix.databinding.FragmentRandomBinding
import com.example.trackflix.model.Trackable

/**
 * A simple [Fragment] subclass.
 */
class RandomFragment : Fragment() {
    private lateinit var binding: FragmentRandomBinding
    private val args by navArgs<RandomFragmentArgs>()
    private var type = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRandomBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        init()

        binding.rerollBtn.setOnClickListener{
            init()
        }

        binding.trackableType.setOnCheckedChangeListener{ group, checkedId ->
            val radiobutton = binding.trackableType.findViewById<RadioButton>(checkedId)
            val selectedChoice = radiobutton.text.toString()

            if(selectedChoice == "Book" ){
                type = "Book"
            }else if(selectedChoice == "Movie"){
                type = "Movie"
            }else if(selectedChoice == "Series"){
                type = "Series"
            }else if(selectedChoice == "Game"){
                type = "Game"
            }else{
                type = "All"
            }
        }

        return view
    }
    /**
     * This method fills the Textfields with the data of a random trackable object from the list
     * Before the object is randomly chosen the list is filtered for all the objects that are in progress
     * to avoid suggesting media that has already been consumed or has not yet been released
     */

    fun init(){
        val filteredTrackables:List<Trackable>

        if(type != "All") {
            filteredTrackables = args.trackableList.trackables.filter { trackable ->
                ((trackable.type == type)&&((trackable.progressState == "inProgress")||(trackable.progressState == "backlog")))
            }
        }else{
            filteredTrackables = args.trackableList.trackables.filter { trackable ->
                (trackable.progressState == "inProgress")||(trackable.progressState == "backlog")
            }
        }

        if(filteredTrackables.isEmpty()){
            binding.titleTV.text = getString(R.string.randFragError)
            binding.typeTV.isVisible=false
            binding.diffTypeTV.isVisible = false
            binding.goalTV.isVisible = false
            binding.goalTypeTV.isVisible = false
            binding.diffrenceTV.isVisible = false
            binding.textView.isVisible = false
            binding.textView2.isVisible = false
            binding.textView3.isVisible = false
            binding.textView10.isVisible = false
            binding.textView9.isVisible = false
            binding.textView13.isVisible = false
            binding.consumedTV.isVisible = false
            return
        }
        val randTrackable = filteredTrackables.random()

        Log.d("TypeRand",randTrackable.type)

        binding.titleTV.text = randTrackable.title
        binding.consumedTV.text = randTrackable.currentProgress.toString() + ' '
        binding.goalTV.text = randTrackable.goal.toString() + ' '
        var diff = (randTrackable.goal-randTrackable.currentProgress)
        if(diff < 0){
            diff = 0
        }
        binding.diffrenceTV.text = diff.toString()

        if(randTrackable.type == "Book"){
            binding.diffTypeTV.setText(R.string.sides)
            binding.goalTypeTV.setText(R.string.sides)
            binding.typeTV.setText(R.string.sides)
        }else{
            binding.diffTypeTV.setText(R.string.hours)
            binding.goalTypeTV.setText(R.string.hours)
            binding.typeTV.setText(R.string.hours)
        }

        binding.typeTV.isVisible=true
        binding.diffTypeTV.isVisible = true
        binding.goalTV.isVisible = true
        binding.goalTypeTV.isVisible = true
        binding.diffrenceTV.isVisible = true
        binding.textView.isVisible = true
        binding.textView2.isVisible = true
        binding.textView3.isVisible = true
        binding.textView10.isVisible = true
        binding.textView9.isVisible = true
        binding.textView13.isVisible = true
        binding.consumedTV.isVisible = true
    }

}