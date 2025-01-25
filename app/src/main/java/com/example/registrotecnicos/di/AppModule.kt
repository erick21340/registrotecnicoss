package com.example.registrotecnicos.di

import android.content.Context
import androidx.room.Room
import com.example.registrotecnicos.data.local.database.TecnicosDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object  AppModule {

    @Provides
    @Singleton
    fun provideTecnicoDB(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TecnicosDB::class.java,
            "TecnicosDB.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePrioridadDao(tecnicosDB: TecnicosDB) = tecnicosDB.tecnicoDao()

    @Provides
    fun provideTicketDao(tecnicosDB: TecnicosDB) = tecnicosDB.ticketDao()

}