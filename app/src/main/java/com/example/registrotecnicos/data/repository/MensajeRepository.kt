package com.example.registrotecnicos.data.repository

import com.example.registrotecnicos.data.local.dao.MensajeDao
import com.example.registrotecnicos.data.local.database.TecnicosDB
import com.example.registrotecnicos.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MensajeRepository @Inject constructor(
    private val tecnicoDb: TecnicosDB
) {
    // Método para guardar un mensaje
    suspend fun saveMensaje(mensaje: MensajeEntity) {
        tecnicoDb.mensajeDao().save(mensaje)
    }

    // Método para buscar un mensaje por su ID
    suspend fun find(id: Int): MensajeEntity? {
        return tecnicoDb.mensajeDao().find(id)
    }

    // Método para eliminar un mensaje
    suspend fun delete(mensaje: MensajeEntity) {
        tecnicoDb.mensajeDao().delete(mensaje)
    }

    // Método para obtener todos los mensajes
    suspend fun getAll(): Flow<List<MensajeEntity>> {
        return tecnicoDb.mensajeDao().getAll()
    }
}