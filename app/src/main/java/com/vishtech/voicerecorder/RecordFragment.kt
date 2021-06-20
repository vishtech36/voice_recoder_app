package com.vishtech.voicerecorder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vishtech.voicerecorder.databinding.FragmentRecordBinding


class RecordFragment : Fragment(R.layout.fragment_record) {

    private lateinit var binding : FragmentRecordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecordBinding.bind(view)

        binding.recordListBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
        }
    }
}