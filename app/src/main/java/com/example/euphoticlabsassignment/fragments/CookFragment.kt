package com.example.euphoticlabsassignment.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.euphoticlabsassignment.R
import com.example.euphoticlabsassignment.classes.RecommendationAdapter
import com.example.euphoticlabsassignment.models.MindTab
import com.example.euphoticlabsassignment.models.Recommendation
import com.example.euphoticlabsassignment.retrofit.RetrofitInterface
import com.example.euphoticlabsassignment.retrofit.RetrofitObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale


class CookFragment : Fragment() {

    private lateinit var adapter: RecommendationAdapter
    private lateinit var scheduleImage: ImageView
    private lateinit var scheduleOrderTextView: TextView
    private lateinit var scheduleTimeTextView: TextView
    private lateinit var scheduleLayout: LinearLayout
//    private var currentlyScheduledItem: Recommendation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cook, container, false)

        scheduleImage = view.findViewById(R.id.schedule_order_image)
        scheduleOrderTextView= view.findViewById(R.id.schedule_text_view)
        scheduleTimeTextView = view.findViewById(R.id.schedule_time_text)
        scheduleLayout = view.findViewById(R.id.schedule_layout)


        //Waht's on your mind section

        val tabContainer = view.findViewById<LinearLayout>(R.id.mind_tab_container)

// Example data (you can replace with your own data fetching logic)
        val tabData = listOf(
            MindTab(R.mipmap.ic_rice, "Rice Items"),
            MindTab(R.mipmap.ic_indian, "Indian"),
            MindTab(R.mipmap.ic_curries, "Curries"),
            MindTab(R.mipmap.ic_soup, "Soups"),
            MindTab(R.mipmap.ic_desert, "Deserts"),
            MindTab(R.mipmap.ic_snack, "Snacks")
        )

        for (tab in tabData) {
            val tabView = layoutInflater.inflate(R.layout.item_mind_tab, tabContainer, false)
            val imageTab = tabView.findViewById<ImageView>(R.id.image_tab)
            val textTab = tabView.findViewById<TextView>(R.id.text_tab)

            // Set image and text for each tab item
            imageTab.setImageResource(tab.imageRes)
            textTab.text = tab.title

            tabContainer.addView(tabView)
        }





        //Recommendation Section
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_recommendation)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


        val api = RetrofitObject.getInstance().create(RetrofitInterface::class.java)
        api.getRecommendation().enqueue(object : Callback<List<Recommendation>> {
            override fun onResponse(call: Call<List<Recommendation>>, response: Response<List<Recommendation>>){
                if (response.isSuccessful) {
                    val dataList = response.body() ?: emptyList()
                    adapter = RecommendationAdapter(dataList){recommendation ->
                        Log.d("Hello","Hello")
                        showTimeSetterPopup(recommendation)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Recommendation>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch data: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })





        return view
    }
    private fun showTimeSetterPopup(recommendation: Recommendation) {
        val popupView = layoutInflater.inflate(R.layout.popup_time_setter, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val hourPicker = popupView.findViewById<NumberPicker>(R.id.hour_picker)
        val minutePicker = popupView.findViewById<NumberPicker>(R.id.minute_picker)
        val amButton = popupView.findViewById<Button>(R.id.am_button)
        val pmButton = popupView.findViewById<Button>(R.id.pm_button)
        val buttonDelete = popupView.findViewById<TextView>(R.id.button_delete)
        val buttonReschedule = popupView.findViewById<Button>(R.id.button_reschedule)
        val buttonCookNow = popupView.findViewById<Button>(R.id.button_cook_now)
        val buttonClose = popupView.findViewById<ImageButton>(R.id.button_close)

        // Set hour picker properties
        hourPicker.minValue = 1
        hourPicker.maxValue = 12

        // Set minute picker properties
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        // Default selection for AM/PM buttons
        var isAMSelected = true // Default AM selection

        buttonClose.setOnClickListener{
            popupWindow.dismiss()
        }

        amButton.setOnClickListener {
            isAMSelected = true
            amButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            pmButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            amButton.setBackgroundResource(R.drawable.custom_button_background_selected)
            pmButton.setBackgroundResource(R.drawable.custom_button_background)
        }

        pmButton.setOnClickListener {
            isAMSelected = false
            pmButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            amButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            pmButton.setBackgroundResource(R.drawable.custom_button_background_selected)
            amButton.setBackgroundResource(R.drawable.custom_button_background)
        }



        // Set click listeners for buttons
        buttonDelete.setOnClickListener {
            // Handle delete action
            scheduleLayout.visibility = View.GONE
            popupWindow.dismiss()
        }

        buttonReschedule.setOnClickListener {
            // Handle reschedule action
            val hour = hourPicker.value
            val minute = minutePicker.value
            val period = if (isAMSelected) "AM" else "PM"

            // Call your function to handle rescheduling with hour, minute, and period
            handleTimePickerAction(recommendation, hour, minute, period)
            popupWindow.dismiss()
        }

        buttonCookNow.setOnClickListener {
            // Handle cook now action
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR)
            val currentMinute = currentTime.get(Calendar.MINUTE)
            val currentPeriod = if (currentTime.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"



            // Call your function to handle cooking now with hour, minute, and period
            handleTimePickerAction(recommendation, currentHour, currentMinute, currentPeriod)
            popupWindow.dismiss()
        }

        // Show the popup
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Blur background
        val blurBackground = view?.rootView?.findViewById<ViewGroup>(R.id.activity_container)
        blurBackground?.alpha = 0.5f
        popupWindow.setOnDismissListener {
            blurBackground?.alpha = 1.0f
        }

    }
    private fun handleTimePickerAction(recommendation: Recommendation, hour: Int, minute: Int, period: String) {
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, period)

        // Example: You can update UI elements or perform API calls based on the action
        // For demonstration, we're logging the scheduled time
        Log.d("CookFragment", "Scheduled time for ${recommendation.dishName}: $formattedTime")

        // Replace with your logic to handle scheduling or cooking actions
        // Example: Call an API or update UI elements
        // For demonstration, we're updating UI elements directly
        Picasso.get()
            .load(recommendation.imageUrl)
            .placeholder(R.drawable.placeholder) // optional placeholder image
            .into(scheduleImage)
        scheduleOrderTextView.text = recommendation.dishName
        scheduleTimeTextView.text = String.format(Locale.getDefault(), "Scheduled %02d:%02d %s", hour, minute, period)
        scheduleLayout.visibility = View.VISIBLE
//        currentlyScheduledItem = recommendation

        // Example: You might want to call an API to schedule this recommendation at the specified time
        // api.scheduleRecommendation(recommendation.id, hour, minute, period, object : Callback<Boolean> { ... })
    }
//
}

