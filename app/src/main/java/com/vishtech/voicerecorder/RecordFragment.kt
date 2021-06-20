package com.vishtech.voicerecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vishtech.voicerecorder.databinding.FragmentRecordBinding
import java.text.SimpleDateFormat
import java.util.*


class RecordFragment : Fragment(R.layout.fragment_record){

    private lateinit var binding : FragmentRecordBinding
    private var isRecording = false
    private val RECORD_PERMISSION = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 12
    private var mediaRecorder : MediaRecorder ?= null
    private var recordFileName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecordBinding.bind(view)

        binding.recordListBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
        }

        binding.recordBtn.setOnClickListener {
            if(isRecording) {
                stopRecording()
                isRecording = false
                binding.recordBtn.setImageResource(R.drawable.ic_stop_music_48)
            }else{
                // start recording
                if(checkPermissions()) {
                    startRecording()
                    isRecording = true
                    binding.recordBtn.setImageResource(R.drawable.ic_mic_48)
                }
            }
        }
    }

    private fun startRecording() {
        binding.recordTimer.base = SystemClock.elapsedRealtime()
        binding.recordTimer.start()

        val recordPath = requireActivity().getExternalFilesDir("/")?.absolutePath
        val simpleDateFormater = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.ENGLISH)
        val date = Date()
        recordFileName = "Recording_${simpleDateFormater.format(date)}.3gp"

        binding.recordFilename.text = "Recording file name: $recordFileName"

        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setOutputFile("$recordPath/$recordFileName")
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mediaRecorder?.prepare()
        }catch (e: Exception){}

        mediaRecorder?.start()
    }

    private fun stopRecording() {

        binding.recordTimer.stop()

        binding.recordFilename.text = "Recording stoped, file saved: $recordFileName"
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null

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


















