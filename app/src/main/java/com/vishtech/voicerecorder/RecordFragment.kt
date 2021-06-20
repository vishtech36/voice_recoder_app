package com.vishtech.voicerecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vishtech.voicerecorder.databinding.FragmentRecordBinding


class RecordFragment : Fragment(R.layout.fragment_record){

    private lateinit var binding : FragmentRecordBinding
    private var isRecording = false
    private val RECORD_PERMISSION = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 12

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecordBinding.bind(view)

        binding.recordListBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
        }

        binding.recordBtn.setOnClickListener {
            if(isRecording) {
                isRecording = false
                binding.recordBtn.setImageResource(R.drawable.ic_launcher_background)
            }else{
                // start recording
                if(checkPermissions()) {
                    isRecording = true
                    binding.recordBtn.setImageResource(R.drawable.ic_mic_24)
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return if(ActivityCompat.checkSelfPermission(requireContext(), RECORD_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(RECORD_PERMISSION),
                PERMISSION_CODE)
            false
        }
    }
}


















