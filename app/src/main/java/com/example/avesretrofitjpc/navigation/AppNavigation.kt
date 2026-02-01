package com.example.avesretrofitjpc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avesretrofitjpc.screens.RecursoDetalleScreen
import com.example.avesretrofitjpc.screens.RecursosScreen
import com.example.avesretrofitjpc.screens.ZonaDetalleScreen
import com.example.avesretrofitjpc.screens.ZonasScreen
import com.example.avesretrofitjpc.viewmodel.MainViewModel

sealed class AppScreens(val route: String) {
    object Zonas : AppScreens("zonas")
    object ZonaDetalle : AppScreens("zona_detalle")
    object Recursos : AppScreens("recursos")
    object RecursoDetalle : AppScreens("recurso_detalle")
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Zonas.route
    ) {
        composable(AppScreens.Zonas.route) {
            ZonasScreen(
                viewModel = viewModel,
                onZonaClick = {
                    navController.navigate(AppScreens.ZonaDetalle.route)
                }
            )
        }

        composable(AppScreens.ZonaDetalle.route) {
            ZonaDetalleScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onRecursosClick = {
                    navController.navigate(AppScreens.Recursos.route)
                }
            )
        }

        composable(AppScreens.Recursos.route) {
            RecursosScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onRecursoClick = {
                    navController.navigate(AppScreens.RecursoDetalle.route)
                }
            )
        }

        composable(AppScreens.RecursoDetalle.route) {
            RecursoDetalleScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
