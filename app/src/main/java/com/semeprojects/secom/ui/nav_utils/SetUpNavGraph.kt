package com.semeprojects.secom.ui.nav_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.semeprojects.secom.ui.screens.CartScreen
import com.semeprojects.secom.ui.screens.CategoryScreen
import com.semeprojects.secom.ui.screens.HomeScreen
import com.semeprojects.secom.ui.screens.MainScreen
import com.semeprojects.secom.ui.screens.ProductDetailScreen
import com.semeprojects.secom.ui.screens.SignInScreen
import com.semeprojects.secom.ui.screens.SignUpScreen


@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController,

){

    var selectedItemIndex by remember {
        mutableStateOf(0)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(route = Screens.SignIn.route){
            SignInScreen(
                navController
            )
        }

        composable(route = Screens.SignUp.route){
            SignUpScreen(
                navController
            )
        }

        composable(route = Screens.Home.route){
            HomeScreen(
                navController
            )
        }

        composable(route = Screens.Main.route){
            MainScreen(
                selectedItemIndex = selectedItemIndex,
                onSelectedItemChange = {
                    selectedItemIndex = it
                },
                navController
            )
        }

        composable(route = Screens.ProductDetail.route){
            ProductDetailScreen(
                navController
            )
        }

        composable(route = Screens.Cart.route){
            CartScreen(
                navController = navController,
            )
        }

        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                ProductDetailScreen(navController)
            }
        }

        composable("${Screens.Category.route}/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            if (category != null) {
                CategoryScreen(navController)
            }
        }

    }
}