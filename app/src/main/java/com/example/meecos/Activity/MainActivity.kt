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
import com.example.meecos.Fragment.MeetingNotes.MeetingNotesFragment
import com.example.meecos.Fragment.Profile.ProfileFragment
import com.example.meecos.Fragment.Schedule.ScheduleFragment
import com.example.meecos.Fragment.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.speech.RecognizerIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.meecos.Config.PERMISSIONS_CODE
import android.widget.Toast

class MainActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    private lateinit var mSchedulebutton: Button
    private lateinit var mMeetingButton: Button
    private lateinit var mProfileButton: Button

    private lateinit var testButton: Button

    private var sr: SpeechRecognizer? = null

    var home: HomeFragment? = null

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

    override fun onPause() {
        stopListening()
        super.onPause()
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

    // TODO: 音声認識開始 とりあえず対応
    private fun startListening() {
        if (sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(this)
            if (!SpeechRecognizer.isRecognitionAvailable(applicationContext)) {
                Toast.makeText(
                    applicationContext,
                    "音声認識が使えません",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            sr!!.setRecognitionListener(this)
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        sr!!.startListening(intent)
    }

    // TODO: とりあえず対応
    fun start(home: HomeFragment) {
        this.home = home
        startListening()
    }

    // TODO: 音声認識終了 とりあえず対応
    private fun stopListening() {
        if (sr != null) sr!!.destroy()
        sr = null
    }

    // TODO: 音声認識を再開する とりあえず対応
    private fun restartListeningService() {
        stopListening()
        startListening()
    }

    override fun onBeginningOfSpeech() { }

    override fun onBufferReceived(buffer: ByteArray) {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {
        var reason = ""
        when (error) {
            SpeechRecognizer.ERROR_AUDIO -> reason = "ERROR_AUDIO"
            SpeechRecognizer.ERROR_CLIENT -> reason = "ERROR_CLIENT"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> reason = "ERROR_INSUFFICIENT_PERMISSIONS"
            SpeechRecognizer.ERROR_NETWORK -> reason = "ERROR_NETWORK"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> reason = "ERROR_NETWORK_TIMEOUT"
            SpeechRecognizer.ERROR_NO_MATCH -> reason = "ERROR_NO_MATCH"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> reason = "ERROR_RECOGNIZER_BUSY"
            SpeechRecognizer.ERROR_SERVER -> reason = "ERROR_SERVER"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> reason = "ERROR_SPEECH_TIMEOUT"
        }
        //Toast.makeText(applicationContext, reason, Toast.LENGTH_SHORT).show()
        restartListeningService()
    }

    override fun onEvent(eventType: Int, params: Bundle) {}

    override fun onPartialResults(partialResults: Bundle) {}

    override fun onReadyForSpeech(params: Bundle) {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onResults(results: Bundle) {
        val values = results
            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (values!!.size > 0 && this.home != null) {
            this.home!!.setText(values[0])
            restartListeningService()
        }
    }
}

