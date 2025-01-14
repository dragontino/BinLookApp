package com.example.binchecker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.binchecker.ui.features.bininfo.BankCardInfoScreen
import com.example.binchecker.ui.features.enter.EnterScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Enter.route,
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(
            route = NavDestination.Enter.route,
            enterTransition = {
                enterScreenTransition(
                    expandFrom = AbsoluteAlignment.Left as BiasAbsoluteAlignment.Horizontal
                )
            },
            exitTransition = {
                exitScreenTransition(
                    shrinkTowards = AbsoluteAlignment.Left as BiasAbsoluteAlignment.Horizontal
                )
            }
        ) {
            EnterScreen(
                viewModel = koinViewModel(),
                navigateTo = { args ->
                    when (args) {
                        is CardInfoArguments -> navController.navigate(
                            route = NavDestination.BankCardInfo.createUrl(args)
                        )
                    }
                }
            )
        }

        composable(
            route = NavDestination.BankCardInfo.route,
            enterTransition = {
                enterScreenTransition(
                    expandFrom = AbsoluteAlignment.Right as BiasAbsoluteAlignment.Horizontal
                )
            },
            exitTransition = {
                exitScreenTransition(
                    shrinkTowards = AbsoluteAlignment.Right as BiasAbsoluteAlignment.Horizontal
                )
            },
            arguments = listOf(
                navArgument(NavDestination.BankCardInfo.BANK_CARD_INFO_ID_KEY) {
                    type = NavType.LongType
                }
            )
        ) {
            BankCardInfoScreen(
                viewModel = koinViewModel {
                    parametersOf(
                        it.arguments!!.getLong(NavDestination.BankCardInfo.BANK_CARD_INFO_ID_KEY)
                    )
                },
                navigateBack = navController::popBackStack,
                navigateToMap = {  }
            )
        }
    }
}