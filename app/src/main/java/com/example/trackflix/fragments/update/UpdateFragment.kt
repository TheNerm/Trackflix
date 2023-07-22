package com.example.trackflix.fragments.update

import android.os.Bundle
import android.util.Log
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
import com.example.trackflix.databinding.FragmentUpdateBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.viewModel.TrackableViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

/**
 * The Update Fragment holds the information about the Trackable entries in the list and provides
 * an interface for the user to track and update the data
 */
class UpdateFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private lateinit var currentTrackable: Trackable
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var myTrackableViewModel: TrackableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        //extraction of the parameter "trackable" from the companion object
        currentTrackable = arguments?.getParcelable<Trackable>("trackable")!!
        Log.d("UpdateFragment", currentTrackable.toString())

        if (currentTrackable==null){
            return view
        }

        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)

        //Inititialising the current fragment with the data from the current trackable
        binding.trackableTitle.setText(currentTrackable.title)
        binding.trackableGoal.setText(currentTrackable.goal.toString())
        binding.trackableCompleted.setText(currentTrackable.currentProgress.toString())
        binding.priorityRB.rating = currentTrackable.prio
        when(currentTrackable.type){
            "Book" -> {
                binding.trackableType.check(binding.book.id)
                binding.tVGoalType.setText(R.string.sides)
                binding.tVWatchedType.setText(R.string.sides)}
            "Movie" -> {
                binding.trackableType.check(binding.movie.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)}
            "Series" -> {
                binding.trackableType.check(binding.series.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)}
            "Game" -> {
                binding.trackableType.check(binding.game.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)
            }
        }
        when(currentTrackable.progressState){
            "backlog" -> {
                binding.trackableProgressionState.check(binding.backlog.id) }
            "inProgress" -> {
                binding.trackableProgressionState.check(binding.inProgress.id) }
            "finished" -> {
                binding.trackableProgressionState.check(binding.finished.id) }
            "cancelled" -> {
                binding.trackableProgressionState.check(binding.cancelled.id)
            }
        }

        //Listener for the button to save entered data from the user
        binding.button.setOnClickListener{
            updateTrackable()
        }

        //provides a contunes change of the type when the category of the medium is changed
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

        return view
    }

    /**
     * This Fuunction reads all the Data from the fields in the fragment and overrides the
     * data int the Database with the read data
     */
    private fun updateTrackable() {
        if(inputCheck()) {
            //replace this with actual data read from our view
            val title = binding.trackableTitle.text.toString()
            val goal = Integer.parseInt(binding.trackableGoal.text.toString())
            val type: String
            val prio = binding.priorityRB.rating
            val progress = Integer.parseInt(binding.trackableCompleted.text.toString())
            when (binding.trackableType.checkedRadioButtonId) {
                binding.book.id -> type = binding.book.text.toString()
                binding.movie.id -> type = binding.movie.text.toString()
                binding.series.id -> type = binding.series.text.toString()
                binding.game.id -> type = binding.game.text.toString()
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Please fill out all fields!",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }
            val progressionState = when(binding.trackableProgressionState.checkedRadioButtonId){
                binding.backlog.id -> TrackableProgressionState.BACKLOG.value
                binding.inProgress.id -> TrackableProgressionState.IN_PROGRESS.value
                binding.finished.id -> TrackableProgressionState.FINISHED.value
                binding.cancelled.id -> TrackableProgressionState.CANCELLED.value
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Please fill out all fields!",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }
            //update trackable
            val updatedTrackable =
                currentTrackable.let { Trackable(it.id, title, progress, goal,type,prio, progressionState) }
            myTrackableViewModel.updateTrackable(updatedTrackable)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Function to check fields that have to be filled
     */
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
        val progressState = when(binding.trackableProgressionState.checkedRadioButtonId){
            binding.backlog.id -> TrackableProgressionState.BACKLOG.value
            binding.inProgress.id -> TrackableProgressionState.IN_PROGRESS.value
            binding.finished.id -> TrackableProgressionState.FINISHED.value
            binding.cancelled.id -> TrackableProgressionState.CANCELLED.value
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Please fill out all fields!",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        if(checkTitle.isEmpty() || checkGoal.isEmpty() || checkType.isEmpty() || progressState.isEmpty()){
            return false
        }

        try{
            if(checkGoal.toInt()<=0){
                return false
            }
        }catch(e: Exception){
            return false
        }

        return true
    }

    /**
     * Companion object for the fragment. Whenever the newInstance Function is called a new instance of
     * this fragment is created with a trackable as parameter. In the oncreate the trackable is extracted and
     * the fragment is filled with this trackables data
     */
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(trackable: Trackable): UpdateFragment{
            val fragment = UpdateFragment()
            val args = Bundle().apply {
                putParcelable("trackable", trackable)
            }
            fragment.arguments = args
            return fragment
        }

    }
}