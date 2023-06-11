package com.example.trackflix.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var myTrackableViewModel: TrackableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)

        binding.trackableTitle.setText(args.currentTrackable.title)
        binding.trackableGoal.setText(args.currentTrackable.goal.toString())
        when(args.currentTrackable.type){
            "Book" -> binding.trackableType.check(binding.book.id)
            "Movie" -> binding.trackableType.check(binding.movie.id)
            "Series" -> binding.trackableType.check(binding.series.id)
            "Game" -> binding.trackableType.check(binding.game.id)
        }

        binding.button.setOnClickListener{
            updateTrackable()
        }

        //addDeleteOptionsMenu
        setHasOptionsMenu(true)

        return view
    }

    private fun updateTrackable() {
        if(inputCheck()) {
            //replace this with actual data read from our view
            val title = binding.trackableTitle.text.toString()
            val goal = Integer.parseInt(binding.trackableGoal.text.toString())
            val type: String
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
            val updatedTrackable = Trackable(args.currentTrackable.id, title, args.currentTrackable.currentProgress, goal,type)
            myTrackableViewModel.updateTrackable(updatedTrackable)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteTrackable()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTrackable() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            myTrackableViewModel.deleteTrackable(args.currentTrackable)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentTrackable.title}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete ${args.currentTrackable.title}?")
        builder.setMessage("Are you sure you want to delete ${args.currentTrackable.title}?")
        builder.create().show()

    }


//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment UpdateFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            UpdateFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}