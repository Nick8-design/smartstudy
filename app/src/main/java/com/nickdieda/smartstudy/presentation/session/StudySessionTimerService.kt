package com.nickdieda.smartstudy.presentation.session

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.nickdieda.smartstudy.util.Constants.ACTION_SERVICE_CANCEL
import com.nickdieda.smartstudy.util.Constants.ACTION_SERVICE_START
import com.nickdieda.smartstudy.util.Constants.ACTION_SERVICE_STOP
import com.nickdieda.smartstudy.util.Constants.NOTIFICATION_CHANNEL_ID
import com.nickdieda.smartstudy.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.nickdieda.smartstudy.util.Constants.NOTIFICATION_ID
import com.nickdieda.smartstudy.util.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StudySessionTimerService: Service() {

    private val binder=StudySessionTimerBinder()
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private lateinit var  timer: Timer

    var duration: Duration= Duration.ZERO
        private set



    var seconds = mutableStateOf("00")
    private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var subjectId = mutableStateOf<Int?>(null)
    var currentTimerState = mutableStateOf(TimerState.IDLE)
    private set

    override fun onBind(intent: Intent?): IBinder =binder

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags:Int, startId:Int):Int{
        intent?.action.let {
            when(it){
                ACTION_SERVICE_START->{
                    startForegroundService()
                    startTimer { hours, minutes, seconds ->
                        updateNotification(hours,minutes,seconds)

                    }

                }

                ACTION_SERVICE_STOP->{

                    stopTimer()
                }

                ACTION_SERVICE_CANCEL->{
                    stopTimer()
                    cancelTimer()
                    stopForegroundService()

                }

            }
        }
        return super.onStartCommand(intent, flags, startId)
    }





    private fun stopTimer(){
        if(this::timer.isInitialized){
            timer.cancel()
        }
        currentTimerState.value= TimerState.STOPPED
    }





    private fun cancelTimer(){
       duration= Duration.ZERO
        updateTimeUnits()
        currentTimerState.value= TimerState.IDLE
    }




    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopForegroundService(){
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

    }




    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,notificationBuilder.build() )
    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val channel= NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)

    }


    private fun startTimer(
        onTick:(hours:String,minutes:String,seconds:String)->Unit
    ){
        currentTimerState.value= TimerState.STARTED
        timer= fixedRateTimer(initialDelay = 1000L, period = 1000L){
            duration=duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value,minutes.value,seconds.value)
        }

    }



    private fun updateNotification(h:String,m:String,s:String){
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText("$h:$m:$s")
                .build()

        )
    }


    private  fun updateTimeUnits(){
        duration.toComponents { hours, minutes, seconds,nano->
            this@StudySessionTimerService.hours.value=hours.toInt().pad()
            this@StudySessionTimerService.minutes.value=minutes.pad()
            this@StudySessionTimerService.seconds.value=seconds.pad()


        }
    }
inner  class StudySessionTimerBinder: Binder(){

    fun getService(): StudySessionTimerService=this@StudySessionTimerService
}


}

enum class TimerState{
    IDLE,
    STARTED,
    STOPPED
}