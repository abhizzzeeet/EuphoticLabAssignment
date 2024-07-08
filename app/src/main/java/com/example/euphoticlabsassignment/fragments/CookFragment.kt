package com.example.euphoticlabsassignment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.euphoticlabsassignment.R
import com.example.euphoticlabsassignment.classes.RecommendationAdapter
import com.example.euphoticlabsassignment.models.MindTab
import com.example.euphoticlabsassignment.models.Recommendation
import com.example.euphoticlabsassignment.retrofit.RetrofitInterface
import com.example.euphoticlabsassignment.retrofit.RetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CookFragment : Fragment() {

    private lateinit var adapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cook, container, false)

        //Waht's on your mind section

        val tabContainer = view.findViewById<LinearLayout>(R.id.mind_tab_container)

// Example data (you can replace with your own data fetching logic)
        val tabData = listOf(
            MindTab(R.mipmap.ic_rice, "Rice Items"),
            MindTab(R.mipmap.ic_rice, "Indian"),
            MindTab(R.mipmap.ic_rice, "Curries"),
            MindTab(R.mipmap.ic_rice, "Soups"),
            MindTab(R.mipmap.ic_rice, "Deserts"),
            MindTab(R.mipmap.ic_rice, "Snacks")
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
                    adapter = RecommendationAdapter(dataList)
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


}