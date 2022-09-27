package com.example.saudiliveradio.presentation

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.saudiliveradio.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider


@AndroidEntryPoint
class MyService : Service() {

    @Inject
    lateinit var exoPlayerProvider: Provider<ExoPlayer>
    private val mBinder: IBinder = MyBinder()
    var streamUrl: String? = null
    var player: ExoPlayer? = null



    inner class MyBinder : Binder() {

        val service: MyService
            get() =
                this@MyService
    }

    override fun onBind(intent: Intent): IBinder? {
       return mBinder
    }

    fun getplayerInstance(): ExoPlayer? {
        return player
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        streamUrl = intent?.getStringExtra("url")

        startNotification()
        initPlayer()

        return START_STICKY
    }

    private fun initPlayer(){

        if (player == null){
            player = exoPlayerProvider.get()
        }

    val mediaItem: MediaItem = MediaItem.fromUri(streamUrl.toString())

    player?.setMediaItem(mediaItem)
    player?.prepare()
    player?.play()
}

    private fun startNotification(){
        val CHANNELID = "foreground service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNELID, CHANNELID, NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, CHANNELID)
            .setContentText(getString(R.string.notifcation_text))
            .setContentTitle(getString(R.string.notifcation_title))
            .setSmallIcon(R.mipmap.ic_launcher)

        startForeground(1000, notification.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
        player = null

    }
}