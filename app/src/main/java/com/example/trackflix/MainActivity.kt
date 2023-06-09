package com.example.trackflix

import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trackflix.database.Trackable
import com.example.trackflix.database.TrackableViewModel
import com.example.trackflix.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var myTrackableViewModel: TrackableViewModel
    private var categories = ArrayList<String>()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        myTrackableViewModel = ViewModelProvider(this)[TrackableViewModel::class.java]
        categories.add("book")
        categories.add("movie")
        categories.add("series")
        categories.add("game")


    }


    public fun getCategories(): ArrayList<String>{
        return categories
    }

    public fun insertDataToDatabase(){

        //replace this with actual data read from our view
        val title = "TestBook"
        val goal = 100
        //use: [TrackableType from <String in lowercase>]
        val type = "book"



        if(inputCheck(title, goal, type) && type!=null){
            //id is primary key and will be auto-generated, so we just need to specify to start at 0
            val trackable = Trackable(0, title, 0, goal, type)
            //add data to database
            myTrackableViewModel.addTrackable(trackable)
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title: String, goal: Int, type: String): Boolean {
        return !(TextUtils.isEmpty(title) || goal==0 || TextUtils.isEmpty(type) || !categories.contains(type))
    }


}