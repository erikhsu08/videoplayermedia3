package com.videoplayermedia3

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

//Guarda informacoes sobre o video
data class MetaData (
    val fileName: String
)

// Qualquer classe que implemente MetaDataReader precisa ser capaz de pegar metadados de um video dado o seu Uri
interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}

//Recebe uma instancia do Application, que permite acessar o contentResolver
class MetaDataReaderImpl(
    private val app: Application
): MetaDataReader {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? { //lê o nome do arquivo a partir do Uri
        if(contentUri.scheme != "content"){ //verifica se o esquema do URI é "content" -> garante que o URI veio do content provider (galeria de midia, nesse caso)
            return null
        }
        val fileName = app.contentResolver //faz uma consulta ao MediaStore
            .query(
                contentUri,
                arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME), //pede para o android retornar o nome do arquivo associado ao Uri
                null,
                null,
                null,
            )
            ?.use { cursor -> //le o resultado
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME) //abre o cursor, pega o indice da coluna com o nome do video e retorna o texto
                cursor.moveToFirst()
                cursor.getString(index)
            }
        return fileName?.let { fullFileName -> //cria e retorna um objeto MetaData
            MetaData(
                fileName = Uri.parse(fullFileName).lastPathSegment ?: return null //garante q pega só o ultimo pedaço do caminho do arquivo (arquivo.mp4)
            )
        }
    }
}