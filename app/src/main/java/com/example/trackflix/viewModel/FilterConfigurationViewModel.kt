package com.example.trackflix.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trackflix.database.TrackableType
import com.example.trackflix.fragments.list.SortingCriteria

class FilterConfigurationViewModel : ViewModel() {
    private val _selectedSortCriterium = MutableLiveData<SortingCriteria>()
    val selectedSortCriterium: LiveData<SortingCriteria> get() = _selectedSortCriterium

    private val _selectedTypeFilters = MutableLiveData<List<TrackableType>>()
    val selectedTypeFilters: LiveData<List<TrackableType>> get() = _selectedTypeFilters

    // Create methods to update the LiveData properties
    fun setSortCriterium(sortingCriterium: SortingCriteria) {
        _selectedSortCriterium.value = sortingCriterium
    }

    fun addSelectedTypeFilters(selectedTypeFilters: List<TrackableType>) {
        _selectedTypeFilters.value = selectedTypeFilters
    }
}