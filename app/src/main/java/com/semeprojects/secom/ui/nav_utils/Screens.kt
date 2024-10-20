package com.semeprojects.secom.ui.nav_utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

sealed class Screens(
    val route: String,
    @StringRes val resourceId: Int = 0,
    val icon: @Composable () -> Unit = {}
) {
    data object SignUp : Screens(
        route = "SignUp"
    )

    data object SignIn : Screens(
        route = "SignIn"
    )

    data object Main: Screens(
        route = "Main"
    )

    data object Home: Screens(
        route = "Home"
    )

    data object ProductDetail: Screens(
        route = "ProductDetail"
    )

    data object Cart : Screens(
        route = "CartScreen"
    )

    data object Category : Screens(
        route = "CategoryScreen"
    )
}