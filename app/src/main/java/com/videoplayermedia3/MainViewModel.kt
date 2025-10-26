package com.videoplayermedia3

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val savedStanteHandle: SavedStateHandle // guarda e restaura o estado da ViewModel (msm após rotação de tela, por exemplo)
    val player: Player //player pra poder realizar as ações de play, pause, passar pro prox video, etc
    private val metaDataReader: MetaDataReader
): ViewModel() {

    private val videoUris = savedStanteHandle.getStateFlow("videoUris", emptyList<Uri>()) //videoUris é uma lista reativa de URIs que o ViewModel mantém


    //Para cada uri, cria um VideoItem com contentUri (propria uri), mediaItem(obj do Media3 q o player entende), name (nome generico).
    val videoItems = videoUris.map { uris ->
        uris.map { uri ->
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()) //converte em flow pra stateflow (fluxo ativo e q mantem o ultimo valor)

    init{
        player.prepare() //prepara o ExoPlayer para reproducao (sem o prepare() o player ainda nao esta inicializado para tocar)
        // assim q a ViewModel nasce, o player já fica pronto para tocar videos msm q nao tenha nenhum
    }

    fun addVideoUri(uri: Uri){
        savedStanteHandle["videoUris"] = videoUris.value + uri //atualiza o estado da viewmodel
        player.addMediaItem(MediaItem.fromUri(uri)) //cria um MediaItem e add ele na fila de repdroucao do player
    }

    fun playVideo(uri: Uri){
        player.setMediaItem(
            videoItems.value.find { it.contentUri == uri }?.mediaItem ?: return //procura dentro da lista o videoItem que corresponde à uri, se achar ele retorna o mediaItem, se nao ele retorna
        )
    }

    override fun onCleared() { //chamado automaticamente qnd a ViewModel é destruida (ex: sair da tela)
        super.onCleared()
        player.release() //libera todos os recursos do ExoPlayer
    }
}