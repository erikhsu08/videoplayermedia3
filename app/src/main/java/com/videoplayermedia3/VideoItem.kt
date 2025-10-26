package com.videoplayermedia3

import android.net.Uri
import androidx.media3.common.MediaItem

//Guarda info necessarias para reproduzir e identificar um video.
data class VideoItem(
    val contentUri: Uri, // endereço real do arquivo de video no dispositivo ou internet
    val mediaItem: MediaItem, // objeto da biblioteca androidx.media3.common q representa um item de midia pronto para ser tocado pelo player
    val name: String // nome do video, vindo do MetaDataReader
)
