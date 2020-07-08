package com.example.meecos.Fragment.MeetingNotes

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R
import java.io.File
import java.time.LocalDateTime
import java.util.*

class RecordFragment : BaseFragment(),View.OnClickListener,
    OnCompletionListener{
    var bt = arrayOfNulls<Button>(3)
    var mp: MediaPlayer? = null
    var mr: MediaRecorder? = null
    var bl1 = booleanArrayOf(true, false, true)
    var bl2 = booleanArrayOf(false, true, false)
    var fl: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_record, container, false)
        setTitle("録音ページ")

        bt[0] = view.findViewById<Button>(R.id.recBtn)
        bt[1] = view.findViewById<Button>(R.id.stopBtn)
        bt[2] = view.findViewById<Button>(R.id.resumeBtn)
        for (i in bt.indices) {
            bt[i]!!.setOnClickListener(this)
        }
        /*fl = File(activity?.getFilesDir(), "meekos${LocalDateTime.now()}.3gp") //録音ファイルの作成*/
        fl = File(activity?.getFilesDir(), "meekos${LocalDateTime.now()}.3gp") //録音ファイルの作成

        return view
    }

    override fun onResume() {
        super.onResume()
        mp = MediaPlayer() //メディアプレーヤークラスのオブジェクトの作成
        mr = MediaRecorder() //メディアレコーダークラスのオブジェクトの作成
        mp!!.setOnCompletionListener(this)
    }

    override fun onPause() {
        super.onPause()
        mp!!.release() //メディアプレーヤーの開放
        mr!!.release() //メディアレコーダーの開放
    }

    override fun onCompletion(mp: MediaPlayer) {
        for (i in bt.indices) {
            bt[i]!!.isEnabled = bl1[i]
        }
    }

    override fun onClick(v: View) {
        if (v === bt[0]) {
            for (i in bt.indices) {
                bt[i]!!.isEnabled = bl2[i]
            }
            try {
                mp!!.reset()
                mr!!.setAudioSource(MediaRecorder.AudioSource.MIC) //
                mr!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) //メディアレコーダーの設定
                mr!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) //
                mr!!.setOutputFile(fl!!.absolutePath)
                mr!!.prepare() //録音の準備
                mr!!.start() //録音の開始
            } catch (e: Exception) {
                println(e.message)
            }
        }
        if (v === bt[1]) {
            for (i in bt.indices) {
                bt[i]!!.isEnabled = bl1[i]
            }
            try {
                mr!!.stop() //録音の停止
            } catch (e: Exception) {
            }
        }
        if (v === bt[2]) {
            for (i in bt.indices) {
                bt[i]!!.isEnabled = false
            }
            try {
                mp!!.setDataSource(fl!!.absolutePath)
                mp!!.prepare()
                mp!!.start()
            } catch (e: Exception) {
            }
        }
    }
}