package com.example.registrotecnicos.data.repository

import com.example.registrotecnicos.data.local.dao.TicketDao
import com.example.registrotecnicos.data.local.entities.TicketEntity
import javax.inject.Inject

class  TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = ticketDao.save(ticket)

    suspend fun update(ticket: TicketEntity) = ticketDao.update(ticket)

    suspend fun get(id: Int) = ticketDao.find(id)

    suspend fun delete(ticket: TicketEntity) = ticketDao.delete(ticket)

    fun getAll() = ticketDao.getAll()
}