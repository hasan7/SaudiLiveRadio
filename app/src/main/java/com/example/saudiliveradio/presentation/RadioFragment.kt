package com.example.saudiliveradio.presentation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.saudiliveradio.R
import com.example.saudiliveradio.databinding.FragmentCheckConnectionBinding
import com.example.saudiliveradio.databinding.FragmentRadioBinding
import com.example.saudiliveradio.observeconnectivity.*
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RadioFragment : Fragment() {


    private lateinit var binding: FragmentRadioBinding
    private lateinit var binding2: FragmentCheckConnectionBinding
    private val viewModel: RadioViewModel by viewModels()
    private lateinit var connectivityObserver: ConnectivityObserver
    var mIsConnected:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =FragmentRadioBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var radio11Link: String? = null
        var radio12Link: String? = null
        var radio13Link: String? = null
        var radio14Link: String? = null
        var radio15Link: String? = null
        var progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        observeConnectivity()
        
        viewModel.state.observe(viewLifecycleOwner){
            progressBar.visibility = View.GONE
            binding.channel11.visibility = View.VISIBLE
            binding.channel12.visibility = View.VISIBLE
            binding.channel13.visibility = View.VISIBLE
            binding.channel14.visibility = View.VISIBLE
            binding.channel15.visibility = View.VISIBLE
            it[11]?.let { it1 ->
                loadGifFromURL(it1.radioLogo, binding.root.context, binding.channel11)
                radio11Link = it1.radioStream
            }
            it[12]?.let { it1 ->
                loadGifFromURL(it1.radioLogo, binding.root.context, binding.channel12)
                radio12Link = it1.radioStream
            }
            it[13]?.let { it1 ->
                loadGifFromURL(it1.radioLogo, binding.root.context, binding.channel13)
                radio13Link = it1.radioStream
            }
            it[14]?.let { it1 ->
                loadGifFromURL(it1.radioLogo, binding.root.context, binding.channel14)
                radio14Link = it1.radioStream
            }
            it[15]?.let { it1 ->
                loadGifFromURL(it1.radioLogo, binding.root.context, binding.channel15)
                radio15Link = it1.radioStream
            }
        }


//        viewModel.isSuccessful.observe(
//            viewLifecycleOwner,
//            object : Observer<Boolean> {
//                override fun onChanged(it: Boolean?) {
//                    viewModel.isSuccessful.removeObserver(this)
//
//                    if (it == false) {
//                        findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToCheckConnectionFragment())
//
//
//                    }
//                }
//            }
//        )



        // TODO: maybe update the imgs outside the observe block

        binding.channel11.setOnClickListener {

            this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToRadioPlayerFragment(radio11Link ?: "error"))
        }

        binding.channel12.setOnClickListener {

            this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToRadioPlayerFragment(radio12Link ?: "error"))
        }

        binding.channel13.setOnClickListener {

            this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToRadioPlayerFragment(radio13Link ?: "error"))
        }

        binding.channel14.setOnClickListener {

            this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToRadioPlayerFragment(radio14Link ?: "error"))
        }

        binding.channel15.setOnClickListener {

            this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToRadioPlayerFragment(radio15Link ?: "error"))
        }


    }

    private fun loadGifFromURL(url: String, context: Context, imageview: ImageView){
        Glide
            .with(context)
            .load(url)
            .into(imageview)
    }
    private fun observeConnectivity() {

        viewModel.isReallyConnected()

        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            Log.d(TAG, "observeConnectivity: ${isConnected}")
            if(isConnected){
                viewModel.loadAllRadiosData()
            }
            //https://nezspencer.medium.com/navigation-components-a-fix-for-navigation-action-cannot-be-found-in-the-current-destination-95b63e16152e
            //if i navigated here to checkConnection the app(sometimes) it will crash since observer will excute the code mutiable times
            //cuase the app to crash since(first will navigate succesfully the next one will cuase crash since we moved to new screen!
        }

//        connectivityObserver.observe().onEach { it ->
//            Log.d(TAG, "observeConnectivity: ${it.name}")
//
////            val fragmentManager = this@RadioFragment.parentFragmentManager
////            val f = fragmentManager.findFragmentById(com.example.saudiliveradio.R.id.nav_host_fragment)
//            if (it.name == ConnectivityObserver.Status.Lost.toString()){
//                this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToCheckConnectionFragment())
//            }
//        }.launchIn(lifecycleScope)

        viewModel.navigateToCheckConnection.observe(viewLifecycleOwner){
            if(it == true){
                this.findNavController().navigate(RadioFragmentDirections.actionRadioFragmentToCheckConnectionFragment())
                viewModel.doneNavigtion()
            }
        }
    }


}
