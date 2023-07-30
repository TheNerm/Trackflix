package com.example.trackflix.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trackflix.R
import com.example.trackflix.database.TrackableProgressionState
import com.example.trackflix.databinding.FragmentAddBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.viewModel.TrackableViewModel

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var myTrackableViewModel: TrackableViewModel
    private var categories = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        categories.add("Book")
        categories.add("Movie")
        categories.add("Series")
        categories.add("Game")

        binding.button.setOnClickListener{
            insertDataToDatabase()
        }

        binding.trackableType.setOnCheckedChangeListener{ group, checkedId ->
            val radiobutton = binding.trackableType.findViewById<RadioButton>(checkedId)
            val selectedChoice = radiobutton.text.toString()

            if(selectedChoice == "Book" ){
                binding.tVWatchedType.setText(R.string.sides)
                binding.tVGoalType.setText(R.string.sides)
            }else{
                binding.tVWatchedType.setText(R.string.hours)
                binding.tVGoalType.setText(R.string.hours)
            }
        }

        myTrackableViewModel = ViewModelProvider(this)[TrackableViewModel::class.java]

        return binding.root
    }
    private fun insertDataToDatabase(){

        //check inputs
        if(inputCheck()){
            //replace this with actual data read from our view
            val title = binding.trackableTitle.text.toString()
            val goal = Integer.parseInt(binding.trackableGoal.text.toString())
            val type: String
            val progresstxt = binding.trackableCompleted.text.toString().trim()
            var progress = 0
            val prio = binding.priorityRB.rating
            var progressState = TrackableProgressionState.BACKLOG.value

            if(progresstxt.isNotEmpty()){
                progress = Integer.parseInt(progresstxt)
            }

            when (binding.trackableType.checkedRadioButtonId){
                binding.book.id -> type = binding.book.text.toString()
                binding.movie.id -> type = binding.movie.text.toString()
                binding.series.id -> type = binding.series.text.toString()
                binding.game.id -> type = binding.game.text.toString()
                else -> {
                    Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
                    return
                }
            }

            if(progress in 1 until goal){
                progressState = TrackableProgressionState.IN_PROGRESS.value
            }else if(progress >= goal){
                progressState = TrackableProgressionState.FINISHED.value
            }
            //id is primary key and will be auto-generated, so we just need to specify to start at 0
            val reldate = ""
            val trackable = Trackable(0, title, progress, goal, type,prio, progressState, reldate)
            //add data to database
            myTrackableViewModel.addTrackable(trackable)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(): Boolean {
        val checkTitle = binding.trackableTitle.text.toString()
        val checkGoal = binding.trackableGoal.text.toString()
        val checkType: String
        when (binding.trackableType.checkedRadioButtonId){
            binding.book.id -> checkType = binding.book.text.toString()
            binding.movie.id -> checkType = binding.movie.text.toString()
            binding.series.id -> checkType = binding.series.text.toString()
            binding.game.id -> checkType = binding.game.text.toString()
            else -> {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
                return false
            }
        }

        if(checkTitle.isEmpty() || checkGoal.isEmpty() || checkType.isEmpty()){
            return false
        }

        try{
            if(checkGoal.toInt()<=0){
                return false
            }
        }catch(e: Exception){
            return false
        }

        if(!categories.contains(checkType)){
            return false
        }

        return true
    }
}