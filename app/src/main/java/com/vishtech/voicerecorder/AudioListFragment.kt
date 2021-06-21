package com.vishtech.voicerecorder

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vishtech.voicerecorder.databinding.FragmentAudioListBinding
import com.vishtech.voicerecorder.databinding.PlayerSheetBinding
import java.io.File


class AudioListFragment : Fragment(R.layout.fragment_audio_list), onItemListClick {

    private lateinit var binding: FragmentAudioListBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var fileToPlay: File? = null
    private lateinit var playerSheetLayout: ConstraintLayout
    private lateinit var playBtn: ImageButton
    private lateinit var playerHeaderTitle: TextView
    private lateinit var playerFilename: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var updateSeekbar: Runnable
    private lateinit var seekBarHandler: Handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAudioListBinding.bind(view)
        playerSheetLayout = view.findViewById(R.id.player_sheet)
        playBtn = view.findViewById(R.id.play_btn)
        playerHeaderTitle = view.findViewById(R.id.player_header_title)
        playerFilename = view.findViewById(R.id.player_filename)
        seekBar = view.findViewById(R.id.player_seek_bar)

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
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do not need this method
            }
        })

        playBtn.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                if (fileToPlay == null) {
                    resumeAudio()
                } else {
                    resumeAudio()
                }
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                pauseAudio()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress: Int = seekBar!!.progress
                mediaPlayer.seekTo(progress)
                resumeAudio()
            }
        })

    }

    override fun onClickListener(file: File, position: Int) {

        fileToPlay = file

        if (isPlaying) {
            stopAudio()
            playAudio(fileToPlay!!)
        } else {
            playAudio(fileToPlay!!)
        }
    }

    private fun pauseAudio() {
        playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        mediaPlayer.pause()
        isPlaying = false
        seekBarHandler.removeCallbacks(updateSeekbar)
    }

    private fun resumeAudio() {
        playBtn.setImageResource(R.drawable.ic_pause_24)
        mediaPlayer.start()
        isPlaying = true

        updateRunnable()
        seekBarHandler.postDelayed(updateSeekbar, 0)
    }

    private fun stopAudio() {
        playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        playerHeaderTitle.text = "Stopped"
        isPlaying = false
        mediaPlayer.stop()
        seekBarHandler.removeCallbacks(updateSeekbar)
    }

    private fun playAudio(file: File) {
        mediaPlayer = MediaPlayer()
        playerHeaderTitle.text = "Playing"
        val bottomSheetBehavior = BottomSheetBehavior.from(playerSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        try {
            mediaPlayer.setDataSource(fileToPlay?.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()

        } catch (e: Exception) {

        }

        playBtn.setImageResource(R.drawable.ic_pause_24)
        playerFilename.text = fileToPlay?.name

        isPlaying = true

        mediaPlayer.setOnCompletionListener {
            stopAudio()
            playerHeaderTitle.text = "Stopped"
        }

        seekBar.max = mediaPlayer.duration
        seekBarHandler = Handler()

        updateRunnable()
        seekBarHandler.postDelayed(updateSeekbar, 0)
    }

    private fun updateRunnable() {
        updateSeekbar = object : Runnable {
            override fun run() {
                seekBar.progress = mediaPlayer.currentPosition
                seekBarHandler.postDelayed(this, 100)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(isPlaying)
            stopAudio()
    }
}