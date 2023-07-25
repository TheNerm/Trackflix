package com.example.trackflix.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.trackflix.databinding.DialogFragmentBinding

class FilterDialogFragment: DialogFragment() {

    private lateinit var binding: DialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentBinding.inflate(layoutInflater, container, false)



        return binding.root
    }
}