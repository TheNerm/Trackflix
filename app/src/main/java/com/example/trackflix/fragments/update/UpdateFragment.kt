package com.example.trackflix.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trackflix.R
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

        currentTrackable = arguments?.getParcelable<Trackable>("trackable")!!
        Log.d("UpdateFragment", currentTrackable.toString())

        if (currentTrackable==null){
            return view
        }

        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)

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

        binding.button.setOnClickListener{
            updateTrackable()
        }

        return view
    }

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
            //update user
            val updatedTrackable =
                currentTrackable.let { Trackable(it.id, title, progress, goal,type,prio) }
            if (updatedTrackable != null) {
                myTrackableViewModel.updateTrackable(updatedTrackable)
            }
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
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

        return true
    }


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