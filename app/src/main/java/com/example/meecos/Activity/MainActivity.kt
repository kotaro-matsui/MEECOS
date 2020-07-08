package com.example.meecos.Activity

import android.Manifest.permission.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meecos.Config.PERMISSIONS_CODE
import com.example.meecos.Fragment.Meeting.MeetingNotesFragment
import com.example.meecos.Fragment.Profile.ProfileFragment
import com.example.meecos.Fragment.Schedule.AlarmNotification
import com.example.meecos.Fragment.Schedule.ScheduleFragment
import com.example.meecos.Fragment.home.HomeFragment
import com.example.meecos.R
import com.google.android.material.navigation.NavigationView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import com.example.meecos.Fragment.Customer.CustomerFragment
import java.util.*

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    private lateinit var mScheduleButton: Button
    private lateinit var mCustomerButton: Button
    private lateinit var mMeetingButton: Button
    private lateinit var mProfileButton: Button

    private lateinit var testButton: Button

    //通知テスト用
    private var am: AlarmManager? = null
    private var pending: PendingIntent? = null
    private val requestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Realmの実装
        val config = RealmConfiguration.Builder()
            // .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        // Realmの実装終わり

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

    ///////////////////////////////////////////通知テスト//////////////////////////////////////
        val buttonStart: Button = findViewById(R.id.pushBtn)
        //pushBtn(通知テスト）を押した時に処理開始
        buttonStart.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            // 1sec
            calendar.add(Calendar.SECOND, 1)
            val intent = Intent(applicationContext, AlarmNotification::class.java)
            intent.putExtra("RequestCode", requestCode)
            pending = PendingIntent.getBroadcast(
                applicationContext, requestCode, intent, 0
            )

            // アラームをセットする
            am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (am != null) {
                am!!.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis, pending
                )

                // トーストで設定されたことをを表示
                Toast.makeText(
                    applicationContext,
                    "alarm start", Toast.LENGTH_SHORT
                ).show()
                Log.d("debug", "start")
            }
        }

        // アラームの取り消し
        val buttonCancel: Button = findViewById(R.id.cancelBtn)
        buttonCancel.setOnClickListener{
            val indent = Intent(applicationContext, AlarmNotification::class.java)
            val pending = PendingIntent.getBroadcast(
                applicationContext, requestCode, indent, 0
            )

            // アラームを解除する
            val am =
                this@MainActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (am != null) {
                am.cancel(pending)
                Toast.makeText(
                    applicationContext,
                    "alarm cancel", Toast.LENGTH_SHORT
                ).show()
                Log.d("debug", "cancel")
            } else {
                Log.d("debug", "null")
            }
        }
        ///////////////////////////////////////////ここまで通知テスト//////////////////////////////////////
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

    /*//スケジュール画面で予定削除した時にEditOrDeleteFragmentから渡される処理
    override fun onDialogPositiveClick(dialog: DialogFragment,isError:Boolean) {
        if(isError){
            Toast.makeText(this, "削除に失敗しました。", Toast.LENGTH_SHORT).show()
        }else{
            replaceFragment(ScheduleFragment())
            Toast.makeText(this, "削除に成功しました。", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }*/


}

