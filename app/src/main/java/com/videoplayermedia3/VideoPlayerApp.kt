package com.videoplayermedia3

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


// Essa classe faz o Hilt funcionar no projeto
//Sem ela, o Hilt nao consegue injetar dependencias nos componentes Android

// Importante colocar essa classe la no Manifest, no android:name, pra q o Android use ela ao inves da classe padrao
@HiltAndroidApp
class VideoPlayerApp: Application() //herda de Application()