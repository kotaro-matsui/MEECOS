package com.example.meecos.Activity

import android.Manifest.permission.*
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
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.meecos.R
import com.example.meecos.Fragment.Meeting.MeetingNotesFragment
import com.example.meecos.Fragment.Profile.ProfileFragment
import com.example.meecos.Fragment.Schedule.ScheduleFragment
import com.example.meecos.Fragment.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.meecos.Config.PERMISSIONS_CODE
import com.example.meecos.Fragment.Customer.CustomerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    private lateinit var mScheduleButton: Button
    private lateinit var mCustomerButton: Button
    private lateinit var mMeetingButton: Button
    private lateinit var mProfileButton: Button

    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.mScheduleButton = findViewById(R.id.schedule_button)
        this.mCustomerButton = findViewById(R.id.customer_button)
        this.mMeetingButton = findViewById(R.id.meeting_button)
        this.mProfileButton = findViewById(R.id.profile_button)

        this.mScheduleButton.setOnClickListener { replaceFragment(ScheduleFragment()) }
        this.mCustomerButton.setOnClickListener { replaceFragment(CustomerFragment()) }
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

        val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
        if (granted == PackageManager.PERMISSION_GRANTED) {
            return
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                RECORD_AUDIO,
                ACCESS_FINE_LOCATION,
                ACCESS_NETWORK_STATE),
            PERMISSIONS_CODE)
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
        fragmentTransaction.addToBackStack(null)
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

