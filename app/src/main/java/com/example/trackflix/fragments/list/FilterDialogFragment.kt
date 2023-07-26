package com.example.trackflix.fragments.list

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.example.trackflix.R
import com.example.trackflix.database.TrackableType
import com.example.trackflix.databinding.DialogFragmentBinding
import com.example.trackflix.viewModel.FilterConfigurationViewModel

class FilterDialogFragment(private val filterConfigurationViewModel: FilterConfigurationViewModel): DialogFragment() {

    private lateinit var currentSortingCriteria:SortingCriteria
    private lateinit var currentActiveFilters:List<TrackableType>
    private var currentlyDescending:Boolean=true
    private lateinit var binding: DialogFragmentBinding

    private lateinit var radioGroup: RadioGroup
    private lateinit var createdRb: RadioButton
    private lateinit var priorityRb: RadioButton
    private lateinit var alphabeticalRb: RadioButton
    private lateinit var completionRb: RadioButton
    private lateinit var spentRb: RadioButton
    private lateinit var expectedRb: RadioButton

    private lateinit var bookCheckbox: CheckBox
    private lateinit var movieCheckbox: CheckBox
    private lateinit var seriesCheckbox: CheckBox
    private lateinit var gameCheckbox: CheckBox

    private lateinit var descSwitch: SwitchCompat


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        binding = DialogFragmentBinding.inflate(LayoutInflater.from(context))
        setupDialog()
        configureUI()
        builder.setView(binding.root)
            .setPositiveButton("Apply") { dialog, which ->
                dismiss()
            }
            .setNegativeButton("Clear") { dialog, which ->
                dismiss()
                clearFilters()
            }

        return builder.create()
    }

    private fun setupDialog(){
        currentSortingCriteria = filterConfigurationViewModel.selectedSortCriterium.value!!
        currentActiveFilters = filterConfigurationViewModel.selectedTypeFilters.value!!
        currentlyDescending = filterConfigurationViewModel.descending.value!!

        radioGroup = binding.sortRb
        createdRb = binding.createdRb
        priorityRb = binding.priorityRb
        alphabeticalRb = binding.alphabeticalRb
        completionRb = binding.completionRb
        spentRb = binding.spentRb
        expectedRb = binding.expectedRb

        bookCheckbox = binding.bookCheckbox
        movieCheckbox = binding.movieCheckbox
        seriesCheckbox = binding.seriesCheckbox
        gameCheckbox = binding.gameCheckbox

        descSwitch = binding.descSwitch
        descSwitch.setOnClickListener {
            descSwitch.text = when(descSwitch.isChecked){
                true -> getString(R.string.descending)
                false -> getString(R.string.ascending)
            }
        }
    }

    private fun configureUI(){
        when(currentSortingCriteria){
            SortingCriteria.CREATIONDATE -> radioGroup.check(createdRb.id)
            SortingCriteria.PRIORITY -> radioGroup.check(priorityRb.id)
            SortingCriteria.ALPHABETICAL -> radioGroup.check(alphabeticalRb.id)
            SortingCriteria.COMPLETION -> radioGroup.check(completionRb.id)
            SortingCriteria.HOURSSPENT -> radioGroup.check(spentRb.id)
            SortingCriteria.EXPECTEDHOURS -> radioGroup.check(expectedRb.id)
        }

        currentActiveFilters.forEach {
            when(it.value){
                TrackableType.BOOK.value -> bookCheckbox.isChecked = true
                TrackableType.MOVIE.value -> movieCheckbox.isChecked = true
                TrackableType.SERIES.value -> seriesCheckbox.isChecked = true
                TrackableType.GAME.value -> gameCheckbox.isChecked = true
            }
        }
        descSwitch.isChecked = currentlyDescending
        descSwitch.text = when(currentlyDescending){
            true -> getString(R.string.descending)
            false -> getString(R.string.ascending)
        }
    }

    private fun clearFilters(){
        filterConfigurationViewModel.setSortCriterium(SortingCriteria.PRIORITY)
        filterConfigurationViewModel.setSelectedFilters(ArrayList<TrackableType>())
        filterConfigurationViewModel.setDescending(true)
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val selectedSortCriterium = when(radioGroup.checkedRadioButtonId){
            createdRb.id -> SortingCriteria.CREATIONDATE
            priorityRb.id -> SortingCriteria.PRIORITY
            alphabeticalRb.id -> SortingCriteria.ALPHABETICAL
            completionRb.id -> SortingCriteria.COMPLETION
            spentRb.id -> SortingCriteria.HOURSSPENT
            expectedRb.id -> SortingCriteria.EXPECTEDHOURS
            else -> {SortingCriteria.CREATIONDATE}
        }
        filterConfigurationViewModel.setSortCriterium(selectedSortCriterium)

        val selectedFilters = ArrayList<TrackableType>()
        if(bookCheckbox.isChecked){
            selectedFilters.add(TrackableType.BOOK)
        }
        if(movieCheckbox.isChecked){
            selectedFilters.add(TrackableType.MOVIE)
        }
        if(seriesCheckbox.isChecked){
            selectedFilters.add(TrackableType.SERIES)
        }
        if(gameCheckbox.isChecked){
            selectedFilters.add(TrackableType.GAME)
        }
        filterConfigurationViewModel.setSelectedFilters(selectedFilters)
        filterConfigurationViewModel.setDescending(descSwitch.isChecked)
    }
}
