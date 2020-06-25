package com.example.meecos.Activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.meecos.R
import com.example.meecos.ui.MeetingNotes.MeetingNotesFragment
import com.example.meecos.ui.Profile.ProfileFragment
import com.example.meecos.ui.Schedule.ScheduleFragment
import com.example.meecos.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    private lateinit var mSchedulebutton: Button
    private lateinit var mMeetingButton: Button
    private lateinit var mProfileButton: Button

    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.mSchedulebutton = findViewById(R.id.schedule_button)
        this.mMeetingButton = findViewById(R.id.meeting_button)
        this.mProfileButton = findViewById(R.id.profile_button)

        this.mSchedulebutton.setOnClickListener { replaceFragment(ScheduleFragment()) }
        this.mMeetingButton.setOnClickListener { replaceFragment(MeetingNotesFragment()) }
        this.mProfileButton.setOnClickListener { replaceFragment(ProfileFragment()) }


        // テストコード　スタート
        this.testButton = findViewById(R.id.test_button)
        this.testButton.setOnClickListener { replaceFragment(HomeFragment()) }
        // テストコード　エンド

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun setTitle (title: String) {
        this.toolbar.title = title
    }

    fun setSubTitle (subtitle: String) {
        this.toolbar.title = subtitle
    }
}
