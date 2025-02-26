package com.example.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Articulos")
data class ArticuloEntity(
    @PrimaryKey(autoGenerate = true)
    val articuloId: Int,
    val descripcion: String,
    val costo: Double,
    val ganancia: Double,
    val precio: Double
)