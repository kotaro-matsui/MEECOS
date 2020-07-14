package com.example.meecos.Fragment.Schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


//端末再起動時に通知を再セットするクラス
class BootBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Intent.ACTION_BOOT_COMPLETED == intent?.action){
            //TODO:端末再起動時にプッシュ通知を再度登録し直す処理
        }
    }
}