 package com.example.registrotecnicos.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.registrotecnicos.presentation.component.NavigationDrawer
import com.example.registrotecnicos.presentation.screens.home.HomeScreen
import com.example.registrotecnicos.presentation.screens.tecnico.IndexTecnicoScreen
import com.example.registrotecnicos.presentation.screens.tickets.CreateTicketScreen
import com.example.registrotecnicos.presentation.screens.tickets.DeleteTicketsScreen
import com.example.registrotecnicos.presentation.screens.tickets.EditTicketScreen
import com.example.registrotecnicos.presentation.screens.tickets.IndexTicketScreen


@Composable
fun NavigationNavHost(
    navHostController: NavHostController
) {

    val isDrawerVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navHostController, startDestination = Screen.HomeScreen) {


            // Home
            composable<Screen.HomeScreen> {
                HomeScreen(
                    onDrawerToggle = { isDrawerVisible.value = !isDrawerVisible.value }
                )
            }


            // Tecnicos
            composable<Screen.IndexTecnicoScreen> {
                IndexTecnicoScreen(
                    onDrawerToggle = { isDrawerVisible.value = !isDrawerVisible.value },
                )
            }


            // Tickets
            composable<Screen.IndexTicketScreen> {
                IndexTicketScreen(
                    onDrawerToggle = { isDrawerVisible.value = !isDrawerVisible.value },
                    onCreateTicket = {
                        navHostController.navigate(Screen.CreateTicketScreen)
                    },
                    onEditTicket = {
                        navHostController.navigate(Screen.EditTicketScreen(it))
                    },
                    onDeleteTicket = {
                        navHostController.navigate(Screen.DeleteTicketScreen(it))
                    }
                )
            }

            composable<Screen.CreateTicketScreen> {
                CreateTicketScreen(
                    onDrawerToggle = {
                        isDrawerVisible.value = !isDrawerVisible.value
                    },
                    goToTicket = {
                        navHostController.navigate(Screen.IndexTicketScreen)
                    }
                )
            }

            composable<Screen.EditTicketScreen> { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getInt("ticketId")
                if (ticketId != null) {

                    EditTicketScreen(
                        ticketId = ticketId,
                        onDrawerToggle = {
                            isDrawerVisible.value = !isDrawerVisible.value
                        },
                        goToTicket = {
                            navHostController.navigate(Screen.IndexTicketScreen)
                        }
                    )
                }
            }

            composable<Screen.DeleteTicketScreen> { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getInt("ticketId")
                if (ticketId != null) {

                    DeleteTicketsScreen(
                        ticketId = ticketId,
                        onDrawerToggle = {
                            isDrawerVisible.value = !isDrawerVisible.value
                        },
                        goToTicket = {
                            navHostController.navigate(Screen.IndexTicketScreen)
                        }
                    )
                }
            }

        }
    }





    NavigationDrawer(
        isVisible = isDrawerVisible.value,
        onItemClick = { itemTitle ->
            when (itemTitle) {
                "Inicio" -> navHostController.navigate(Screen.HomeScreen)
                "Tecnicos" -> navHostController.navigate(Screen.IndexTecnicoScreen)
                "Tickets" -> navHostController.navigate(Screen.IndexTicketScreen)
            }
            isDrawerVisible.value = false
        },
        onClose = {
            isDrawerVisible.value = false
        }
    )
}


