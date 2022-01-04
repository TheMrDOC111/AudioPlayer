package com.adtarassov.audioplayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.databinding.AudioItemBinding

class AudioListAdapter(
  private val onAudioClickListener: (AudioModel) -> Unit
) : RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {

  private var audioList = ArrayList<AudioModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
    val binding = AudioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AudioViewHolder(binding, onAudioClickListener)
  }

  override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
    holder.bind(audioList[position])
  }

  override fun getItemCount(): Int = audioList.size

  fun refreshAudioList(list: List<AudioModel>) {
    audioList.clear()
    audioList.addAll(list)
    notifyItemRangeInserted(0, list.size)
  }

  class AudioViewHolder(
    private val binding: AudioItemBinding,
    private val onAudioClickListener: (AudioModel) -> Unit
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: AudioModel) {
      binding.root.setOnClickListener {
        onAudioClickListener(model)
      }
      binding.apply {
        title.text = model.title
        subtitle.text = model.subtitle
      }
    }
  }

}