package com.example.meecos.Config

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener):
    RecognitionListener {

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