package com.allinedelara.dog.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Dog : BottomNavItem("dog", Icons.Default.Home, "Home")
    object Favorite : BottomNavItem("favorite", Icons.Default.Favorite, "Favorite")
}
