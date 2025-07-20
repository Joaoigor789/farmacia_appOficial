package com.example.farmacia_app

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.util.Log
import java.util.*

class AppMonitorService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                verificarBateria()
                verificarAtividades()
            }
        }, 0, 3600000) // 1 hora

        return START_STICKY
    }

    private fun verificarBateria() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        Log.d("AppMonitor", "Nível da bateria: $level%")
    }

    private fun verificarAtividades() {
        val activityManager = applicationContext.getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningProcesses = activityManager.runningAppProcesses
        runningProcesses?.forEach {
            Log.d("AppMonitor", "Processo ativo: ${it.processName}")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
