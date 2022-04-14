package com.adtarassov.audioplayer.ui.sreen.player.smallscreen

import androidx.lifecycle.viewModelScope
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.ui.BaseFlowViewModel
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.HasTrack
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewState.UnActivePayer
import com.adtarassov.audioplayer.utils.player.AudioManager
import com.adtarassov.audioplayer.utils.player.AudioService.Companion.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmallPlayerViewModel @Inject constructor(
  private val audioManager: AudioManager,
) : BaseFlowViewModel<SmallPlayerViewState, SmallPlayerAction, SmallPlayerEvent>() {

  init {
    viewModelScope.launch {
      audioManager.audioServiceFlow.collect { audioService ->
        if (audioService == null) {
          viewState = UnActivePayer
          return@collect
        }
        combine(
          audioService.currentAudioFlow,
          audioService.playerStateFlow,
          audioService.isPlayingFlow
        ) { currentAudio, playerState, isPlaying ->
          CombinedModel(currentAudio, playerState, isPlaying)
        }
          .collect { model ->
            val currentAudio = model.currentAudio
            viewState = if (currentAudio == null) {
              UnActivePayer
            } else {
              HasTrack(currentAudio.title, currentAudio.artist ?: "", model.isPlaying)
            }
          }
      }
    }
  }

  override fun obtainEvent(viewEvent: SmallPlayerEvent) {
    viewModelScope.launch {
      when (viewEvent) {
        is SmallPlayerEvent.OnPlayButtonCLick -> {
          audioManager.audioServiceFlow.collect { audioService ->
            audioService?.playerChangePlayState()
          }
        }
      }
    }
  }

  private class CombinedModel(
    val currentAudio: AudioModel?,
    val playerState: PlayerState,
    val isPlaying: Boolean
  )

}