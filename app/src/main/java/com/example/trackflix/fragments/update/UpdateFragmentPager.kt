package com.example.trackflix.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.trackflix.R
import com.example.trackflix.databinding.FragmentUpdatePagerBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.viewModel.TrackableViewModel
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 */

/**
 * This Fragment provides the Pager for the UpdateFragments. The class is responsible for swiping through
 * the single trackables with their detail optic
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

        //extracting list and current position of the clicked trackable as a startpoint
        trackables = args.currentTrackableList.trackables
        currentPos = args.currentListPosition
        currentTrackable = trackables[currentPos]

        Log.d("UpdateScreen", trackables.toString())

        //Inflating the pager and setting the adapter
        binding = FragmentUpdatePagerBinding.inflate(inflater, container, false)
        val rootView = binding.root
        viewPager = binding.viewPager
        val adapter = UpdatePagerAdapter(requireActivity(), trackables)
        viewPager.adapter = adapter

        viewPager.setCurrentItem(currentPos)

        //changing the current trackable dynamically on user swiping
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

    /**
     * This function is responsible for deleting the right item in the List when the delete button is pressed
     */
    private fun deleteTrackable() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            myTrackableViewModel.deleteTrackable(currentTrackable)
            Toast.makeText(requireContext(), "Successfully removed: ${currentTrackable.title}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete ${currentTrackable.title}?")
        builder.setMessage("Are you sure you want to delete ${currentTrackable.title}?")
        builder.create().show()
    }
}