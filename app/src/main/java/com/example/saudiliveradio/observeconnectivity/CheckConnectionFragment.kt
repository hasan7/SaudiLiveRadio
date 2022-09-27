package com.example.saudiliveradio.observeconnectivity

import android.R
import android.app.Application
import android.content.ContentValues.TAG
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.saudiliveradio.databinding.FragmentCheckConnectionBinding
import com.example.saudiliveradio.presentation.RadioFragment
import com.example.saudiliveradio.presentation.RadioFragmentDirections
import com.example.saudiliveradio.presentation.RadioPlayerFragment
import com.example.saudiliveradio.presentation.RadioPlayerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class CheckConnectionFragment: Fragment(){

    private lateinit var binding: FragmentCheckConnectionBinding
    @Inject lateinit var application: Application
    private val viewModel: CheckConnectionViewModel by viewModels()

    // TODO: Listen for wifi if it connects back to and check again automaitclly

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCheckConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isReallyConnected()
        binding.tryAgainBtn.setOnClickListener { viewModel.isReallyConnected() }

//        viewModel.isSuccessful.observe(viewLifecycleOwner){ successful ->
//
//
//        }

        viewModel.navigateToRadio.observe(viewLifecycleOwner){
            if(it == true){
                this.findNavController().navigate(CheckConnectionFragmentDirections.actionCheckConnectionFragmentToRadioFragment())
                viewModel.doneNavigtion()
            }
        }

    }
}