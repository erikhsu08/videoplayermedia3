package com.videoplayermedia3

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

//Modulo do Hilt, que diz para oHilt como criar e fornecer certas dependencias que serao injetadas nos ViewModels

@Module
@InstallIn(ViewModelComponent::class)
object VideoPlayerModule {
    @Provides
    @ViewModelScoped // garante q o msm player será usado enquanto o ViewModel existir, evitando recriacoes desnecessarias
    // A função abaixo diz ao Hilt como criar um Player (ExoPlayer)
    fun provideVideoPlayer(app: Application): Player { // app: Application -> Hilt injeta automaticamente  a instancia do Application (VideoPlayerApp) aq, pra construir o ExoPlayer
        return ExoPlayer.Builder(app)
            .build()
    }

    @Provides
    @ViewModelScoped // Cada ViewModel tera sua instancia durante sua vida util
    //Cria um objeto que le metadados de videos (MetaDataReaderImpl)
    fun provideMetaDataReader(app: Application): MetaDataReader{
        return MetaDataReaderImpl(app)
    }
}