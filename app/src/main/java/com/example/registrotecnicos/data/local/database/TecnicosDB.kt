package com.example.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrotecnicos.data.local.dao.TecnicoDao
import com.example.registrotecnicos.data.local.dao.TicketDao
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import com.example.registrotecnicos.data.local.entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicosDB : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun ticketDao(): TicketDao
}