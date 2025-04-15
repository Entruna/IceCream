package com.example.icecream.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.icecream.presentation.screen.cart.CartScreen
import com.example.icecream.presentation.screen.icecream.IceCreamScreen

@Composable
fun IceCreamAppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "icecream") {
        composable("icecream") {
            IceCreamScreen(navController = navController)
        }
        composable("cartScreen") {
            CartScreen(navController = navController)
        }
    }
}