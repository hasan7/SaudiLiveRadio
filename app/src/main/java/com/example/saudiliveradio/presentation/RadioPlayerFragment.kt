package com.example.saudiliveradio.presentation

import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.saudiliveradio.databinding.FragmentRadioPlayerBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.util.Util.startForegroundService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RadioPlayerFragment : Fragment() {

    var isPlaying:Boolean = true
    var mService: MyService? = null
    var mIsBound: Boolean = false
    lateinit var  serviceIntent: Intent
    private lateinit var binding: FragmentRadioPlayerBinding
    private val viewModel: RadioPlayerViewModel by viewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as MyService.MyBinder
            mService = binder.service
            mIsBound = true
            initPlayer()

        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            mIsBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRadioPlayerBinding.inflate(inflater, container, false)

        val arguments = RadioPlayerFragmentArgs.fromBundle(requireArguments())

        serviceIntent = Intent(binding.root.context, MyService::class.java)
        serviceIntent.putExtra("url", arguments.radioUrl)

        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(binding.root.context, serviceIntent)
        //}

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            startService()
//        }
        bindService()

        binding.stopButton.setOnClickListener {
            if (isPlaying){
                it.setBackgroundResource( android.R.drawable.ic_media_play)
                context?.stopService(serviceIntent)
                unbindService()
                isPlaying = false
            } else{
                it.setBackgroundResource( android.R.drawable.ic_media_pause)
                startForegroundService(binding.root.context, serviceIntent)
                bindService()
                isPlaying = true
            }
    }
        viewModel.navigateToCheckConnection.observe(viewLifecycleOwner){
            if (it == true){
                this.findNavController().navigate(RadioPlayerFragmentDirections.actionRadioPlayerFragmentToCheckConnectionFragment())
                viewModel.doneNavigtion()
            }
        }

        return binding.root
    }

    fun initPlayer(){
        val playerView = binding.videoView
        playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        val player = mService?.getplayerInstance()
        val progressBar = binding.progressBar
        playerView.player = player

        player?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.d(TAG,"ExoPlayer error, ${error.errorCode},${error.errorCodeName},${error.message}")
                context?.stopService(serviceIntent)
                unbindService()
                isPlaying = false
                binding.stopButton.setBackgroundResource( android.R.drawable.ic_media_play)
                viewModel.navigateToCheckConnection()


            }
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE)
                } else {
                    progressBar.setVisibility(View.INVISIBLE)
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isPlaying){
            unbindService()
            context?.stopService(serviceIntent)
        }
    }

    private fun bindService() {
        context?.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        context?.unbindService(serviceConnection)
    }
}