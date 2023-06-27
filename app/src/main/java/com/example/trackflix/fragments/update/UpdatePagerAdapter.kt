package com.example.trackflix.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.trackflix.model.Trackable

class UpdatePagerAdapter(fragmentActivity: FragmentActivity, private val trackableList: List<Trackable>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return trackableList.size
    }

    override fun createFragment(position: Int): Fragment {
        val trackable = trackableList[position]
        return UpdateFragment.newInstance(trackable)
    }
}
