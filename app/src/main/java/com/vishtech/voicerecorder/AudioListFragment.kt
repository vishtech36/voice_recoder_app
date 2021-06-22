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
    private lateinit var playerSheetLayout: ConstraintLayout
    private lateinit var playBtn: ImageButton
    private lateinit var playNextBtn: ImageButton
    private lateinit var playPrevBtn: ImageButton
    private lateinit var playerHeaderTitle: TextView
    private lateinit var playerFilename: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var updateSeekbar: Runnable
    private lateinit var seekBarHandler: Handler
    private lateinit var allFiles: Array<File>
    private var currentAudioIndex = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAudioListBinding.bind(view)
        playerSheetLayout = view.findViewById(R.id.player_sheet)
        playBtn = view.findViewById(R.id.play_btn)
        playNextBtn = view.findViewById(R.id.play_next_btn)
        playPrevBtn = view.findViewById(R.id.play_prev_btn)
        playerHeaderTitle = view.findViewById(R.id.player_header_title)
        playerFilename = view.findViewById(R.id.player_filename)
        seekBar = view.findViewById(R.id.player_seek_bar)

        val bottomSheetBehavior = BottomSheetBehavior.from(playerSheetLayout)

        // fetching all files
        val path = requireActivity().getExternalFilesDir("/")?.absolutePath
        val directory = File(path)
        allFiles = directory.listFiles()

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
                resumeAudio()
            }
        }

        playNextBtn.setOnClickListener {
            if (isPlaying) {
                stopAudio()
                playNextAudio()
            } else {
                playNextAudio()
            }
        }

        playPrevBtn.setOnClickListener {
            if (isPlaying) {
                stopAudio()
                playPrevAudio()
            } else {
                playPrevAudio()
            }
        }



    seekBar.setOnSeekBarChangeListener(
    object : SeekBar.OnSeekBarChangeListener {
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

    currentAudioIndex = position

    if (isPlaying) {
        stopAudio()
        playAudio()
    } else {
        playAudio()
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

private fun playAudio() {
    mediaPlayer = MediaPlayer()
    playerHeaderTitle.text = "Playing"
    val bottomSheetBehavior = BottomSheetBehavior.from(playerSheetLayout)
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    try {
        mediaPlayer.setDataSource(allFiles[currentAudioIndex].absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()

    } catch (e: Exception) {

    }

    playBtn.setImageResource(R.drawable.ic_pause_24)
    playerFilename.text = allFiles[currentAudioIndex].name

    isPlaying = true

    mediaPlayer.setOnCompletionListener {
        stopAudio()
        playerHeaderTitle.text = "Stopped"
        playNextAudio()
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

private fun playNextAudio() {
    currentAudioIndex = (++currentAudioIndex) % allFiles.size
    playAudio()
}

private fun playPrevAudio() {
    if(currentAudioIndex == 0) {
        currentAudioIndex = allFiles.size
    }
    currentAudioIndex--
    playAudio()

}

override fun onStop() {
    super.onStop()
    if (isPlaying)
        stopAudio()
}
}