package com.example.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Mensajes")
data class MensajeEntity(
    @PrimaryKey
    val mensajeId: Int? = null,
    val tecnicoId: Int? = null,
    val descripcion: String = "",
    val fecha: String? = "",
)