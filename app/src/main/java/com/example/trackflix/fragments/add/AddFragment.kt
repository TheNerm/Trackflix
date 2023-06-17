package com.example.trackflix.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trackflix.R
import com.example.trackflix.model.Trackable
import com.example.trackflix.viewModel.TrackableViewModel
import com.example.trackflix.databinding.FragmentAddBinding
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

        myTrackableViewModel = ViewModelProvider(this)[TrackableViewModel::class.java]

        return binding.root
    }
    public fun insertDataToDatabase(){

        //check inputs
        if(inputCheck()){
            //replace this with actual data read from our view
            val title = binding.trackableTitle.text.toString()
            val goal = Integer.parseInt(binding.trackableGoal.text.toString())
            val type: String
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

            //id is primary key and will be auto-generated, so we just need to specify to start at 0
            val trackable = Trackable(0, title, 0, goal, type)
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

//    public fun addRadioButtons(){
//        val rdgroup = binding.trackableType
//        val categories = (activity as MainActivity?)?.getCategories()
//        if (categories!= null){
//            for (category in categories){
//                val rdbtn = RadioButton(activity)
//                rdbtn.id = View.generateViewId()
//                rdbtn.text = category
//                rdgroup.addView(rdbtn)
//            }
//        }
//    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AddFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic fun newInstance(param1: String, param2: String) =
//                AddFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}