package com.example.trackflix.fragments.list

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ProgressBar
import androidx.core.view.GestureDetectorCompat
import com.example.trackflix.database.TrackableType
import com.example.trackflix.model.Trackable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomProgressBar
    @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    ) : ProgressBar(context, attrs, defStyleAttr),  GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {
    //gestures
    private var doubleTapDetected = false
    lateinit var mDetector:GestureDetectorCompat
    //asynchronous job
    var progressJob: Job? = null
    //stuff for updating
    lateinit var updateTrackableVisuals: (ListAdapter.MyViewHolder, trackable: Trackable)->Unit
    lateinit var trackable:Trackable
    lateinit var holder:ListAdapter.MyViewHolder
    lateinit var progressUpdater: ProgressUpdater


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            if(event.action == MotionEvent.ACTION_CANCEL){
                stopMotion()
            }
            super.onTouchEvent(event)
        }
    }


    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        //This is called when a long-press is detected
        if (doubleTapDetected) {
            Log.d("track", "Element is being held after a double-tap")
            progressJob = startUpdatingProgress()

        }
    }

    override fun onSingleTapConfirmed(p0: MotionEvent): Boolean {
        Log.d("track", "single tap confirmed")
        performClick()
        return true
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        //This is called when a double-tap is detected
        doubleTapDetected = true
        Log.d("track", "start double tap")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        //This is called for each event in a double-tap sequence
        Log.d("track", MotionEvent.actionToString(e.action))
        if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_HOVER_EXIT) {
            Log.d("track", "stop double tap")
            doubleTapDetected = false
            stopUpdatingProgress()
            progressUpdater.updateProgress(trackable)
        }
        return true
    }


    override fun onShowPress(p0: MotionEvent) {


    }


    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {

        return true
    }


    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {

        return true
    }

    private fun startUpdatingProgress() : Job {
        stopUpdatingProgress() //Ensure we don't have multiple instances running

        val delay:Long = when(trackable.type){
            TrackableType.BOOK.value -> 85
            else -> 450
        }
        return CoroutineScope(Dispatchers.Main).launch {
            var currentProgress = trackable.currentProgress
            while (currentProgress < Integer.MAX_VALUE) {
                currentProgress += 1
                trackable = Trackable(trackable.id, trackable.title, currentProgress, trackable.goal, trackable.type, trackable.prio, trackable.progressState, trackable.releaseDate)
                updateTrackableVisuals(holder, trackable)
                delay(delay) // Delay in milliseconds between each increment
            }
        }
    }

    private fun stopMotion(){
        doubleTapDetected = false
        stopUpdatingProgress()
        progressUpdater.updateProgress(trackable)
    }

    private fun stopUpdatingProgress() {
        progressJob?.cancel()
    }

    interface ProgressUpdater{
        fun updateProgress(trackable: Trackable)
    }

}