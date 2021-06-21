package com.vishtech.voicerecorder

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vishtech.voicerecorder.databinding.FragmentAudioListBinding
import java.io.File


class AudioListFragment : Fragment(R.layout.fragment_audio_list), onItemListClick {

    private lateinit var binding : FragmentAudioListBinding
    private lateinit var playerSheetLayout: ConstraintLayout
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private lateinit var fileToPlay: File

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAudioListBinding.bind(view)
        playerSheetLayout = view.findViewById<ConstraintLayout>(R.id.player_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(playerSheetLayout)

        // fetching all files
        val path = requireActivity().getExternalFilesDir("/")?.absolutePath
        val directory = File(path)
        val allFiles = directory.listFiles()

        val adapter = AudioListAdapter(allFiles, this)
        binding.audioListView.apply {
            setHasOptionsMenu(true)
            setAdapter(adapter)
        }
        // to handle on swipe down event
        bottomSheetBehavior.addBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do not need this method
            }

        })

    }

    override fun onClickListener(file: File, position: Int) {
        if(isPlaying) {
            stopAudio()
            playAudio(fileToPlay)
        } else {
            fileToPlay = file
            playAudio(fileToPlay)
        }
    }

    private fun stopAudio() {

        isPlaying = false
    }

    private fun playAudio(file: File) {
        mediaPlayer = MediaPlayer()
        try{
            mediaPlayer.setDataSource(fileToPlay.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }catch (e: Exception){

        }
        isPlaying = true
    }


}