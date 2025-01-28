package com.allinedelara.dog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.allinedelara.dog.ui.screen.DogScreen
import com.allinedelara.dog.ui.screen.FavoriteScreen

@Composable
fun AppNavGraph (){
    val navController = rememberNavController()

    // Get current backstack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold (
        bottomBar = {
            BottomNavigationBar(
                navController = navController, items = listOf(
                    BottomNavItem.Dog,
                    BottomNavItem.Favorite,
                )
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = NavigationItem.Dog.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Dog.route){
                DogScreen()
            }
            composable(NavigationItem.Favorite.route){
                FavoriteScreen()
            }
        }
    }
}
