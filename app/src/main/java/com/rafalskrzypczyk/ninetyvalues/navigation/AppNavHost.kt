package com.rafalskrzypczyk.ninetyvalues.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rafalskrzypczyk.ninetyvalues.presentation.entries_history.EntriesHistoryScreen
import com.rafalskrzypczyk.ninetyvalues.presentation.entries_history.EntriesHistoryVM
import com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen.EntryScreen
import com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen.EntryVM
import com.rafalskrzypczyk.ninetyvalues.presentation.home_screen.HomeScreen
import com.rafalskrzypczyk.ninetyvalues.presentation.home_screen.HomeScreenVM
import com.rafalskrzypczyk.ninetyvalues.presentation.new_entry.NewEntryScreen
import com.rafalskrzypczyk.ninetyvalues.presentation.new_entry.NewEntryVM

@Composable
fun AppNavHost (navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        enterTransition = {
            scaleIn(initialScale = 0.75f) + fadeIn()
        },
        exitTransition = {
            scaleOut(targetScale = 1.25f) + fadeOut()
        },
        popEnterTransition = {
            scaleIn(initialScale = 1.25f) + fadeIn()
        },
        popExitTransition = {
            scaleOut(targetScale = 0.75f) + fadeOut()
        }
    ) {
        composable("home") {
            val vm = hiltViewModel<HomeScreenVM>()
            HomeScreen(
                state = vm.state.collectAsStateWithLifecycle().value,
                onNavigateToEntriesList = { navController.navigate("entries_history") },
                onNavigateToNewEntry = { navController.navigate("new_entry") }
            )
        }
        composable("new_entry") {
            val vm = hiltViewModel<NewEntryVM>()
            NewEntryScreen(
                state = vm.state.collectAsStateWithLifecycle().value,
                onEvent = vm::onEvent,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSavedEntry = { entryId ->
                    navController.navigate("entry/$entryId") {
                        popUpTo("new_entry") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "entry/{entryId}",
            arguments = listOf(
                navArgument("entryId") { type = NavType.LongType }
            )
        ) {
            val vm = hiltViewModel<EntryVM>()
            EntryScreen(
                state = vm.state.collectAsStateWithLifecycle().value,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("entries_history") {
            val vm = hiltViewModel<EntriesHistoryVM>()
            EntriesHistoryScreen(
                state = vm.state.collectAsStateWithLifecycle().value,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEntry = { entryId -> navController.navigate("entry/$entryId") }
            )
        }
    }
}