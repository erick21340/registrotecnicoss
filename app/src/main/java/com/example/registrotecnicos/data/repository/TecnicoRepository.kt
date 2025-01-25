package com.example.registrotecnicos.data.repository

import com.example.registrotecnicos.data.local.dao.TecnicoDao
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import javax.inject.Inject

class TecnicoRepository @Inject constructor(
    private val tecnicoDao: TecnicoDao
) {
    suspend fun save(ticket: TecnicoEntity) = tecnicoDao.save(ticket)

    suspend fun get(id: Int) = tecnicoDao.find(id)

    suspend fun delete(ticket: TecnicoEntity) = tecnicoDao.delete(ticket)

    fun getAll() = tecnicoDao.getAll()
}