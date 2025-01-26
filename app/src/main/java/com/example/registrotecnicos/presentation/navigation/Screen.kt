package com.example.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class  Screen {
    //Home
    @Serializable
    data object HomeScreen : Screen()

    //Tecnico
    @Serializable
    data object IndexTecnicoScreen : Screen()

    //Ticket
    @Serializable
    data object IndexTicketScreen : Screen()
    @Serializable
    data object CreateTicketScreen : Screen()
    @Serializable
    data class EditTicketScreen(val ticketId: Int?) : Screen()
    @Serializable
    data class DeleteTicketScreen(val ticketId: Int?) : Screen()
    @Serializable
    data object IndexmensajeCreen: Screen()
}