package com.example.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrotecnicos.data.local.dao.ArticuloDao
import com.example.registrotecnicos.data.local.dao.MensajeDao
import com.example.registrotecnicos.data.local.dao.TecnicoDao
import com.example.registrotecnicos.data.local.dao.TicketDao

import com.example.registrotecnicos.data.local.entities.ArticuloEntity
import com.example.registrotecnicos.data.local.entities.MensajeEntity
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import com.example.registrotecnicos.data.local.entities.TicketEntity
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
        MensajeEntity::class,
        ArticuloEntity:: class,
    ],
    version = 5,
    exportSchema = false
)
abstract class  TecnicosDB : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun ticketDao(): TicketDao
    abstract fun mensajeDao(): MensajeDao
    abstract fun ArticuloDao(): ArticuloDao
}