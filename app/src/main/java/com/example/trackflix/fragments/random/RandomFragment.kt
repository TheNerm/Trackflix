package com.example.trackflix.fragments.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trackflix.R
import com.example.trackflix.databinding.FragmentRandomBinding
import com.example.trackflix.viewModel.TrackableViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [RandomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RandomFragment : Fragment() {
    private lateinit var myTrackableViewModel: TrackableViewModel
    private lateinit var binding: FragmentRandomBinding
    private val args by navArgs<RandomFragmentArgs>()

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

        binding.backBtn.setOnClickListener{
            findNavController().navigate((R.id.action_randomFragment_to_listFragment))
        }

        binding.rerollBtn.setOnClickListener{
            init()
        }

        return view
    }
    /**
     * This method fills the Textfields with the data of a random trackable object from the list
     * Before the object is randomly chosen the list is filtered for all the objects that are in progress
     * to avoid suggesting media that has already been consumed or has not yet been released
     */

    fun init(){
        val filteredTrackables = args.trackableList.trackables.filter{trackable ->
            trackable.progressState == "inProgress"
        }
        val randTrackable = filteredTrackables.random()

        binding.titleTV.text = randTrackable.title
        binding.consumedTV.text = randTrackable.currentProgress.toString()
        binding.goalTV.text = randTrackable.goal.toString()
        binding.diffrenceTV.text = (randTrackable.goal-randTrackable.currentProgress).toString()

        if(randTrackable.type == "Book"){
            binding.diffTypeTV.setText(R.string.sides)
            binding.goalTypeTV.setText(R.string.sides)
            binding.typeTV.setText(R.string.sides)
        }else{
            binding.diffTypeTV.setText(R.string.hours)
            binding.goalTypeTV.setText(R.string.hours)
            binding.typeTV.setText(R.string.hours)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment RandomFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            RandomFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}