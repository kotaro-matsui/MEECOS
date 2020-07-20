package com.example.meecos.Fragment.Meeting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.meecos.Dialog.CommonDialogFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R

class MeetingRecordFragment : BaseFragment(), RecognitionListener, CommonDialogFragment.CommonDialogListener {

    private var sr: SpeechRecognizer? = null

    var mMeetingText: TextView? = null
    private var longText: String? = ""

    private var mStart: Button? = null
    private var mStop: Button? = null

    private var dialog: CommonDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meeting_record, container, false)
        setTitle("記録")

        this.mMeetingText = view.findViewById(R.id.meeting_text)

        this.mStart = view.findViewById(R.id.start)
        this.mStop = view.findViewById(R.id.stop)

        this.mStart!!.setOnClickListener { startListening() }
        this.mStop!!.setOnClickListener { stopListening() }

        this.dialog = CommonDialogFragment.newInstance(
            "議事録",
            "MEECOSが議事録をとります\n[OK]を押すと開始します\n[NO]を押すとHOMEへ戻ります",
            this
            )
        this.dialog!!.show(parentFragmentManager, "dialog")

        return view
    }

    override fun onPause() {
        super.onPause()
        stopListening()
    }

    override fun onOkClick() {
        super.onOkClick()
        this.dialog!!.dismiss()
    }

    override fun onNoClick() {
        super.onOkClick()
        this.dialog!!.dismiss()
    }

    /**
     * 音声認識開始
     */
    private fun startListening() {
        if (sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(activity)
            if (!SpeechRecognizer.isRecognitionAvailable(activity)) {
                Toast.makeText(
                    activity,
                    "音声認識が使えません",
                    Toast.LENGTH_LONG
                ).show()
                return
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

    /**
     * 音声認識終了
     */
    private fun stopListening() {
        if (sr != null) sr!!.destroy()
        sr = null
    }

    /**
     * 音声認識を再開する
     */
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
        if (values!!.size > 0) {
            Handler(Looper.getMainLooper()).post {
                this.longText = longText + "\n" + values[0]
                this.mMeetingText!!.text =this.longText
            }
            restartListeningService()
        }
    }
}