package com.example.trackflix.fragments.list

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackflix.R
import com.example.trackflix.database.TrackableProgressionState
import com.example.trackflix.databinding.FragmentListBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.model.TrackableList
import com.example.trackflix.viewModel.TrackableViewModel
import com.google.android.material.tabs.TabLayout

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
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

    private lateinit var binding: FragmentListBinding
    private lateinit var myTrackableViewModel: TrackableViewModel
    private lateinit var tabLayout: TabLayout
    private val tabStartingIndex = 1
    private lateinit var adapter: ListAdapter
    private lateinit var filterList: List<String>
    private var sortingCriteria: SortingCriteria = SortingCriteria.CREATIONDATE

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_list, container, false)
        //return view

        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        adapter = ListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)
        tabLayout = binding.tabLayout
        myTrackableViewModel.readAllData.observe(viewLifecycleOwner, Observer {trackable ->
            tabLayout.getTabAt(tabStartingIndex)?.select()
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                 updateListVisuals(tab?.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.i("tab", tab?.text.toString()+" reselected")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.i("tab", tab?.text.toString()+" unselected")
            }
        })


        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate((R.id.action_listFragment_to_addFragment))
        }

        binding.randomFAB.setOnClickListener{
            //creating a list of current entries and passing them to the randdm fragment
            val trackables = mutableListOf<Trackable>()
            myTrackableViewModel.readAllData.observe(viewLifecycleOwner){ trackableList->
                trackables.addAll(trackableList)
            }
            val trackList = TrackableList(trackables)
            val action = ListFragmentDirections.actionListFragmentToRandomFragment(trackList)
            Navigation.findNavController(view).navigate(action)
        }

        val dialogFragment = FilterDialogFragment()
        val fragmentManager = childFragmentManager
        //dialogFragment.dialogListener = this
        binding.filtering.setOnClickListener {

            dialogFragment.show(fragmentManager, "dialog_fragment_tag")
        }
        //add menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteAllTrackables()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllTrackables() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            myTrackableViewModel.deleteAllTrackables()
            Toast.makeText(requireContext(), "Successfully removed everything", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete every Trackable?")
        builder.create().show()
    }

    private fun updateListVisuals(newTabIndex : Int?){
        val filteredTrackables = myTrackableViewModel.readAllData.value?.filter {
            when (newTabIndex) {
                0 -> it.progressState == TrackableProgressionState.BACKLOG.value
                1 -> it.progressState == TrackableProgressionState.IN_PROGRESS.value
                2 -> it.progressState == TrackableProgressionState.FINISHED.value
                3 -> it.progressState == TrackableProgressionState.CANCELLED.value
                else -> {
                    true
                }
            }
        }
        adapter.setData(filteredTrackables!!)
    }

// /*   companion object {
//        *//**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ListFragment.
//         *//*
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }*/
}