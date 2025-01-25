package com.example.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.registrotecnicos.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  TicketDao {
    @Upsert
    suspend fun save(ticket: TicketEntity)

    @Update
    suspend fun update(ticket: TicketEntity)

    @Query(
        """
        SELECT * 
        FROM Tickets 
        WHERE ticketId = :id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): TicketEntity?

    @Delete
    suspend fun delete(ticket: TicketEntity)

    @Query("SELECT * FROM Tickets")
    fun getAll(): Flow<List<TicketEntity>>

}