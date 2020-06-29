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
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.meecos.Config.PERMISSIONS_CODE

class MainActivity : AppCompatActivity(), SimpleRecognizerListener.SimpleRecognizerResponseListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    private lateinit var mSchedulebutton: Button
    private lateinit var mMeetingButton: Button
    private lateinit var mProfileButton: Button

    private lateinit var testButton: Button

    private lateinit var mFragment: Fragment

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private var speechState = false

    private var longText: String = ""

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

        setupSpeechRecognizer()
        setupRecognizerIntent()

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

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(SimpleRecognizerListener(this))
    }

    private fun setupRecognizerIntent() {
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false)
    }

    private fun startListening() {
        speechState = true
        speechRecognizer.startListening(recognizerIntent)
    }

    private fun stopListening() {
        speechState = false
        speechRecognizer.stopListening()
    }

    override fun onResultsResponse(speechText: String) {
        var text = this.longText + "\n" + speechText
        this.longText = text
        startListening()
    }

    override fun restart() {
        startListening()
    }

    fun speech(/* listener: Fragment? */) {
        if (speechState) {
            stopListening()
        } else {
            startListening()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                for (i: Int in permissions.indices) {
                    if ((permissions[i] == ACCESS_FINE_LOCATION) && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        Handler(Looper.getMainLooper()).post {
                            (this.mFragment as HomeFragment).setWeatherDetails()
                        }
                    }
                }
            }
        }
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

    fun setCallbackFragment (fragmnet: Fragment) {
        this.mFragment = fragmnet
    }
}

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener): RecognitionListener {

    interface SimpleRecognizerResponseListener {
        fun onResultsResponse(speechText: String)
        fun restart()
    }

    override fun onReadyForSpeech(p0: Bundle?) { }
    override fun onRmsChanged(p0: Float) { }
    override fun onBufferReceived(p0: ByteArray?) { }
    override fun onPartialResults(p0: Bundle?) { }
    override fun onEvent(p0: Int, p1: Bundle?) { }
    override fun onBeginningOfSpeech() { }
    override fun onEndOfSpeech() { }

    override fun onError(p0: Int) {
        var error = ""
        when (p0) {
            SpeechRecognizer.ERROR_AUDIO -> error = "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> error = "Other client side errors"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> error = "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> error = "Network related errors"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> error = "Network operation timed out"
            SpeechRecognizer.ERROR_NO_MATCH -> error = "No recognition result matched"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> error = "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> error = "Server sends error status"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> error = "No speech input"
        }
        listener.restart()
    }

    override fun onResults(bundle: Bundle?) {
        if (bundle == null) {
            listener.onResultsResponse("")
            return
        }

        val key = SpeechRecognizer.RESULTS_RECOGNITION
        val result = bundle.getStringArrayList(key)
        val speechText = result?.get(0)
        if (speechText.isNullOrEmpty()) {
            listener.onResultsResponse("")
        } else {
            listener.onResultsResponse(speechText)
        }
    }
}