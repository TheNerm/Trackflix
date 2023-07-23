package com.example.trackflix.fragments.update

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trackflix.MainActivity
import com.example.trackflix.R
import com.example.trackflix.database.TrackableProgressionState
import com.example.trackflix.databinding.FragmentUpdateBinding
import com.example.trackflix.model.Trackable
import com.example.trackflix.notification.NotificationReceiver
import com.example.trackflix.viewModel.TrackableViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

/**
 * The Update Fragment holds the information about the Trackable entries in the list and provides
 * an interface for the user to track and update the data
 */
class UpdateFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    // TODO: Rename and change types of parameters


    private lateinit var currentTrackable: Trackable
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var myTrackableViewModel: TrackableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        //extraction of the parameter "trackable" from the companion object
        currentTrackable = arguments?.getParcelable<Trackable>("trackable")!!
        Log.d("UpdateFragment", currentTrackable.toString())

        if (currentTrackable==null){
            return view
        }

        myTrackableViewModel = ViewModelProvider(this).get(TrackableViewModel::class.java)

        //Inititialising the current fragment with the data from the current trackable
        binding.trackableTitle.setText(currentTrackable.title)
        binding.trackableGoal.setText(currentTrackable.goal.toString())
        binding.trackableCompleted.setText(currentTrackable.currentProgress.toString())
        binding.priorityRB.rating = currentTrackable.prio
        when(currentTrackable.type){
            "Book" -> {
                binding.trackableType.check(binding.book.id)
                binding.tVGoalType.setText(R.string.sides)
                binding.tVWatchedType.setText(R.string.sides)}
            "Movie" -> {
                binding.trackableType.check(binding.movie.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)}
            "Series" -> {
                binding.trackableType.check(binding.series.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)}
            "Game" -> {
                binding.trackableType.check(binding.game.id)
                binding.tVGoalType.setText(R.string.hours)
                binding.tVWatchedType.setText(R.string.hours)
            }
        }
        when(currentTrackable.progressState){
            "backlog" -> {
                binding.trackableProgressionState.check(binding.backlog.id) }
            "inProgress" -> {
                binding.trackableProgressionState.check(binding.inProgress.id) }
            "finished" -> {
                binding.trackableProgressionState.check(binding.finished.id) }
            "cancelled" -> {
                binding.trackableProgressionState.check(binding.cancelled.id)
            }
        }

        if(currentTrackable.releaseDate.isNotEmpty()){
            binding.releasedSW.isChecked = true
            binding.releaseDateET.visibility = View.VISIBLE
            binding.releaseDateET.setText(currentTrackable.releaseDate)
        }

        //Listener for the button to save entered data from the user
        binding.button.setOnClickListener{
            updateTrackable()
        }

        //Listener to open date picker
        binding.releaseDateET.setOnClickListener{
            showDatePickerDialog()
        }

        //provides a contunes change of the type when the category of the medium is changed
        binding.trackableType.setOnCheckedChangeListener{ group, checkedId ->
            val radiobutton = binding.trackableType.findViewById<RadioButton>(checkedId)
            val selectedChoice = radiobutton.text.toString()

            if(selectedChoice == "Book" ){
                binding.tVWatchedType.setText(R.string.sides)
                binding.tVGoalType.setText(R.string.sides)
            }else{
                binding.tVWatchedType.setText(R.string.hours)
                binding.tVGoalType.setText(R.string.hours)
            }
        }

        binding.releasedSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.releaseDateET.visibility = View.VISIBLE
            } else {
                binding.releaseDateET.visibility = View.GONE
                //Visibility gone will not only make the textfield invisible to the user but also
                //It will only return a null value should it be accessed by code while it is GONE
            }
        }

        return view
    }

    /**
     * This Fuunction reads all the Data from the fields in the fragment and overrides the
     * data int the Database with the read data
     */
    private fun updateTrackable() {
        if(inputCheck()) {
            //replace this with actual data read from our view
            val title = binding.trackableTitle.text.toString()
            val goal = Integer.parseInt(binding.trackableGoal.text.toString())
            val type: String
            val prio = binding.priorityRB.rating
            val progress = Integer.parseInt(binding.trackableCompleted.text.toString())
            when (binding.trackableType.checkedRadioButtonId) {
                binding.book.id -> type = binding.book.text.toString()
                binding.movie.id -> type = binding.movie.text.toString()
                binding.series.id -> type = binding.series.text.toString()
                binding.game.id -> type = binding.game.text.toString()
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Please fill out all fields!",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }
            val progressionState = when(binding.trackableProgressionState.checkedRadioButtonId){
                binding.backlog.id -> TrackableProgressionState.BACKLOG.value
                binding.inProgress.id -> TrackableProgressionState.IN_PROGRESS.value
                binding.finished.id -> TrackableProgressionState.FINISHED.value
                binding.cancelled.id -> TrackableProgressionState.CANCELLED.value
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Please fill out all fields!",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }

            //sending notification if a new releasedate is entered. The entered date has to be in the future or it wouldnt make
            //sense setting a release date
            var reldate: String = binding.releaseDateET.text.toString()

            if(reldate != "00.00.0000"){
                val dateFormat = "dd-MM-yyyy"
                val calendar = stringToCalendar(reldate, dateFormat)
                if(calendar!=null){
                    sendNotification(requireContext(),"Release Notification",
                        "$title should be available now",calendar)
                    if(calendar.timeInMillis <= Calendar.getInstance().timeInMillis){
                        Toast.makeText(requireContext(), "Failed saving date! Target in the past.", Toast.LENGTH_LONG).show()
                        reldate = ""
                    }
                }
            }

            //update trackable
            val updatedTrackable =
                currentTrackable.let { Trackable(it.id, title, progress, goal,type,prio, progressionState,reldate) }
            myTrackableViewModel.updateTrackable(updatedTrackable)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Function to check fields that have to be filled
     */
    private fun inputCheck(): Boolean {
        val checkTitle = binding.trackableTitle.text.toString()
        val checkGoal = binding.trackableGoal.text.toString()
        val checkType: String
        when (binding.trackableType.checkedRadioButtonId){
            binding.book.id -> checkType = binding.book.text.toString()
            binding.movie.id -> checkType = binding.movie.text.toString()
            binding.series.id -> checkType = binding.series.text.toString()
            binding.game.id -> checkType = binding.game.text.toString()
            else -> {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
                return false
            }
        }
        val progressState = when(binding.trackableProgressionState.checkedRadioButtonId){
            binding.backlog.id -> TrackableProgressionState.BACKLOG.value
            binding.inProgress.id -> TrackableProgressionState.IN_PROGRESS.value
            binding.finished.id -> TrackableProgressionState.FINISHED.value
            binding.cancelled.id -> TrackableProgressionState.CANCELLED.value
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Please fill out all fields!",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        if(checkTitle.isEmpty() || checkGoal.isEmpty() || checkType.isEmpty() || progressState.isEmpty()){
            return false
        }

        try{
            if(checkGoal.toInt()<=0){
                return false
            }
        }catch(e: Exception){
            return false
        }

        return true
    }
    //Functions to set the notifications for the release date should one be entered
    private fun createNotificationChannel(context: Context) {
        val channelId = "NotifyOnRelease"
        val channelName = "Release Notification"
        val channelDescription = "Channel for messages to the user about releases of trackables"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    // the sending of the notification to the user
    fun sendNotification(context: Context, title: String, message: String, date: Calendar) {
        createNotificationChannel(context)

        //need to check if the new entered date is really a new later one. If so then the notification with the old id is overwritten
        val dateFormat = "dd-MM-yyyy"
        val oldcal = stringToCalendar(currentTrackable.releaseDate, dateFormat)
        if(oldcal != null&&date.timeInMillis <= oldcal.timeInMillis){
            return
        }

        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("notification_id", currentTrackable.id)
        notificationIntent.putExtra("notification_title", title)
        notificationIntent.putExtra("notification_message", message)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            currentTrackable.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        //The alarm manager is responsible for sheduling the notification at a certain time
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, date.timeInMillis, pendingIntent
        )


        /*val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("notification_title", title)
        intent.putExtra("notification_message", message)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            date.timeInMillis,
            pendingIntent
        )*/
    }

    //Functions to realise a Date picker dialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, dayOfMonth)

        // Format the selected date and display it in the TextView or EditText
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedCalendar.time)
        binding.releaseDateET.setText(formattedDate)
    }

    fun stringToCalendar(dateString: String, dateFormat: String): Calendar? {
        val calendar = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat(dateFormat)
            val date = sdf.parse(dateString)
            if (date != null) {
                calendar.time = date
                return calendar
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Companion object for the fragment. Whenever the newInstance Function is called a new instance of
     * this fragment is created with a trackable as parameter. In the oncreate the trackable is extracted and
     * the fragment is filled with this trackables data
     */
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(trackable: Trackable): UpdateFragment{
            val fragment = UpdateFragment()
            val args = Bundle().apply {
                putParcelable("trackable", trackable)
            }
            fragment.arguments = args
            return fragment
        }

    }
}
