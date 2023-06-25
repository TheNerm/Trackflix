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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.trackflix.R
import com.example.trackflix.databinding.FragmentUpdateBinding
import com.example.trackflix.databinding.FragmentUpdatePagerBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.viewModel.TrackableViewModel
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragmentPager.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragmentPager : Fragment() {
    // TODO: Rename and change types of parameters
    private val args by navArgs<UpdateFragmentPagerArgs>()
    private lateinit var trackables: List<Trackable>
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentUpdatePagerBinding
    private var currentPos by Delegates.notNull<Int>()
    private lateinit var currentTrackable: Trackable
    private lateinit var myTrackableViewModel: TrackableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)

        trackables = args.currentTrackableList.trackables
        currentPos = args.currentListPosition
        currentTrackable = trackables[currentPos]

        Log.d("UpdateScreen", trackables.toString())

        binding = FragmentUpdatePagerBinding.inflate(inflater, container, false)
        val rootView = binding.root
        viewPager = binding.viewPager
        val adapter = UpdatePagerAdapter(requireActivity(), trackables)
        viewPager.adapter = adapter

        viewPager.setCurrentItem(currentPos)

        viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentTrackable = trackables[position]
            }
        })

        //addDeleteOptionsMenu
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
            Toast.makeText(requireContext(), "Successfully removed: ${currentTrackable.title}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete ${currentTrackable.title}?")
        builder.setMessage("Are you sure you want to delete ${currentTrackable.title}?")
        builder.create().show()
        myTrackableViewModel.deleteTrackable(currentTrackable)
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment updateFragmentPager.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            updateFragmentPager().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}