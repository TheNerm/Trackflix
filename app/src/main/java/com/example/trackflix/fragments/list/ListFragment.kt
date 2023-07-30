package com.example.trackflix.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackflix.R
import com.example.trackflix.database.TrackableProgressionState
import com.example.trackflix.database.TrackableType
import com.example.trackflix.databinding.FragmentListBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.model.TrackableList
import com.example.trackflix.viewModel.FilterConfigurationViewModel
import com.example.trackflix.viewModel.TrackableViewModel
import com.google.android.material.tabs.TabLayout


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), CustomProgressBar.ProgressUpdater{
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
    private lateinit var filterConfigurationViewModel: FilterConfigurationViewModel

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_list, container, false)
        //return view

        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupViewModels()
        setupTabs()
        tabLayout.selectTab(tabLayout.getTabAt(tabStartingIndex))
        setupFloatingButtons(view)

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
    private fun setupRecyclerView(){
        adapter = ListAdapter()
        adapter.progressUpdater =this
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun setupViewModels(){
        myTrackableViewModel = ViewModelProvider(this)[TrackableViewModel::class.java]
        filterConfigurationViewModel = ViewModelProvider(this)[FilterConfigurationViewModel::class.java]

        filterConfigurationViewModel.selectedTypeFilters.observe(viewLifecycleOwner) { typeFilters ->
            updateListVisuals(null)
            setFilterButtonColor()
        }
        filterConfigurationViewModel.selectedSortCriterium.observe(viewLifecycleOwner){sortCriterium ->
            updateListVisuals(null)
        }
        filterConfigurationViewModel.descending.observe(viewLifecycleOwner){desc ->
            updateListVisuals(null)
        }
        myTrackableViewModel.readAllData.observe(viewLifecycleOwner) { trackable ->
            updateListVisuals(null)
        }
    }
    private fun setupTabs(){
        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateListVisuals(tab?.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                updateListVisuals(tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setupFloatingButtons(view: View){
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate((R.id.action_listFragment_to_addFragment))
        }

        binding.randomFAB.setOnClickListener{
            //creating a list of current entries and passing them to the random fragment
            val trackableList: List<Trackable>? = myTrackableViewModel.readAllData.value
            val trackList = TrackableList(trackableList!!)
            val action = ListFragmentDirections.actionListFragmentToRandomFragment(trackList)
            Navigation.findNavController(view).navigate(action)
        }

        binding.statisticsFAB.setOnClickListener{
            //passing list to next fragment
            val trackableList: List<Trackable>? = myTrackableViewModel.readAllData.value
            val trackList = TrackableList(trackableList!!)
            val action = ListFragmentDirections.actionListFragmentToStatistics(trackList)
            Navigation.findNavController(view).navigate(action)
        }

        //add menu
        setHasOptionsMenu(true)
        val dialogFragment = FilterDialogFragment(filterConfigurationViewModel)
        val fragmentManager = childFragmentManager
        binding.filtering.setOnClickListener {

            dialogFragment.show(fragmentManager, "dialog_fragment_tag")
        }
    }

    private fun deleteAllTrackables() {
        val builder = AlertDialog.Builder(requireContext())
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

    //gets called each time a tab is switched or a filter is applied
    private fun updateListVisuals(newTabIndex : Int?) {
        val tabIndex:Int? = newTabIndex ?: tabLayout.getTabAt(tabLayout.selectedTabPosition)?.position
        var filteredTrackables = myTrackableViewModel.readAllData.value?.filter {
            when (tabIndex) {
                0 -> it.progressState == TrackableProgressionState.BACKLOG.value
                1 -> it.progressState == TrackableProgressionState.IN_PROGRESS.value
                2 -> it.progressState == TrackableProgressionState.FINISHED.value
                3 -> it.progressState == TrackableProgressionState.CANCELLED.value
                else -> {
                    true
                }
            }
        }?.filter {
            val selectedFilters = filterConfigurationViewModel.selectedTypeFilters.value
            if(selectedFilters.isNullOrEmpty()){
                true
            }else{
                selectedFilters.contains(
                    TrackableType.from(
                        it.type
                    )
                )
            }
        }?.sortedBy {
            when (filterConfigurationViewModel.selectedSortCriterium.value) {
                SortingCriteria.CREATIONDATE -> it.id
                SortingCriteria.PRIORITY -> (it.prio * 2).toInt()
                SortingCriteria.COMPLETION -> (100*(it.currentProgress.toFloat()/it.goal.toFloat())).toInt()
                SortingCriteria.HOURSSPENT -> it.currentProgress
                SortingCriteria.EXPECTEDHOURS -> it.goal
                else -> {
                    it.id
                }
            }
        }
        //for some weird reason alphabetical sorting could not be included in the sortedBy function above
        //apparently if you use a when inside a sortedBy, every return has to be of the same type (integer above)
        if(filterConfigurationViewModel.selectedSortCriterium.value == SortingCriteria.ALPHABETICAL){
            filteredTrackables = filteredTrackables?.sortedBy { it.title }
        }

        if(filterConfigurationViewModel.descending.value == true){
            filteredTrackables = filteredTrackables?.reversed()
        }

        if(filteredTrackables != null){
            adapter.setData(filteredTrackables)
        }
    }

    //change color of filter button to indicate that filters are active
    private fun setFilterButtonColor(){
        val color = if(filterConfigurationViewModel.selectedTypeFilters.value.isNullOrEmpty()){
            R.color.secondaryColor
        }else{
            R.color.teal_700
        }
        val colorStateList = ContextCompat.getColorStateList(requireContext(), color)
        binding.filtering.backgroundTintList = colorStateList
    }

    override fun updateProgress(trackable: Trackable) {
        myTrackableViewModel.updateTrackable(trackable)
    }

}