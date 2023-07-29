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

    private val _descending = MutableLiveData<Boolean>()
    val descending: MutableLiveData<Boolean> get() = _descending

    init {
        _selectedSortCriterium.value = SortingCriteria.PRIORITY
        _selectedTypeFilters.value = ArrayList()
        _descending.value = true
    }

    fun setSortCriterium(sortingCriterium: SortingCriteria) {
        _selectedSortCriterium.value = sortingCriterium
    }

    fun setSelectedFilters(selectedTypeFilters: List<TrackableType>) {
        _selectedTypeFilters.value = selectedTypeFilters
    }

    fun setDescending(descending: Boolean){
        _descending.value = descending
    }
}