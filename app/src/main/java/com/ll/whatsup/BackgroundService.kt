package com.ll.whatsup

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BackgroundService : Service(), Runnable {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("com.ll.whatsup","Background Service Started")

        val r = Runnable(){
             run()
        }

        val th = Thread(r)
        th.start()
        return Service.START_NOT_STICKY
    }

    override fun run() {
        synchronized(this){
            try {
                Thread.sleep(5000)
                Log.i("com.ll.whatsup","thread running")
            }catch (e:Exception){}

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("com.ll.whatsup","Background Service deleted")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}