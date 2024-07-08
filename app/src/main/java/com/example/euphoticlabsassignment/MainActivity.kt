package com.example.euphoticlabsassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.example.euphoticlabsassignment.classes.VerticalTabLayoutAdapter
import com.example.euphoticlabsassignment.fragments.CookFragment
import com.example.euphoticlabsassignment.fragments.DeviceFragment
import com.example.euphoticlabsassignment.fragments.FavouritesFragment
import com.example.euphoticlabsassignment.fragments.ManualFragment
import com.example.euphoticlabsassignment.fragments.PreferencesFragment
import com.example.euphoticlabsassignment.fragments.SettingsFragment
import com.example.euphoticlabsassignment.models.TabItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabItems = listOf(
            TabItem(R.drawable.ic_cook, "Cook"),
            TabItem(R.drawable.ic_favourites, "Favourities"),
            TabItem(R.drawable.ic_manual, "Manual"),
            TabItem(R.drawable.ic_device, "Device"),
            TabItem(R.drawable.ic_preferences, "Preferences"),
            TabItem(R.drawable.ic_settings, "Settings")

        )
        val tabLayout = findViewById<RecyclerView>(R.id.vertical_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.container).apply {
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }

        val tabAdapter = VerticalTabLayoutAdapter(tabItems) { position ->
            viewPager.currentItem = position
        }

        tabLayout.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tabAdapter
        }

        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabAdapter.selectTab(position)
                tabLayout.scrollToPosition(position)
            }
        })
    }
}

// This "ViewPagerAdapter" class overrides functions which are
// necessary to get information about which item is selected
// by user, what is title for selected item and so on.*/
class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 6 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CookFragment()
            1 -> FavouritesFragment()
            2 -> ManualFragment()
            3 -> DeviceFragment()
            4 -> PreferencesFragment()
            5 -> SettingsFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}

