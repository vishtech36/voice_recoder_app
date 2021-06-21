package com.vishtech.voicerecorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vishtech.voicerecorder.databinding.SingleListItemBinding
import java.io.File

class AudioListAdapter(val allFiles: Array<File>, private val onItemListClick: onItemListClick): RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>(),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_list_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.binding.listItemTitle.text = allFiles[position].name
        holder.binding.listItemDetails.text = Util.getTimeAgo(allFiles[position].lastModified())
        holder.listItem.setOnClickListener{

            onItemListClick.onClickListener(allFiles[position], position)
        }
    }

    override fun getItemCount() = allFiles.size

    class AudioViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SingleListItemBinding.bind(itemView)
        val listItem = itemView
    }

    override fun onClick(v: View?) {
    }
}


interface onItemListClick {
    fun onClickListener(file: File, position: Int);
}
