package com.allinedelara.dog.navigation

enum class Screen {
    DOG,
    FAVORITE
}
sealed class NavigationItem(val route: String) {
    object Dog : NavigationItem(Screen.DOG.name)
    object Favorite : NavigationItem(Screen.FAVORITE.name)

}
