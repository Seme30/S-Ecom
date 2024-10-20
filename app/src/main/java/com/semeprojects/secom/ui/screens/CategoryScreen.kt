package com.semeprojects.secom.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.semeprojects.secom.R
import com.semeprojects.secom.data.local.model.CartItem
import com.semeprojects.secom.data.network.model.Product
import com.semeprojects.secom.ui.components.ProductCard
import com.semeprojects.secom.ui.nav_utils.Screens
import com.semeprojects.secom.viewmodel.CartViewModel
import com.semeprojects.secom.viewmodel.CategoryViewModel
import com.semeprojects.secom.viewmodel.ProductUIState

@Composable
fun CategoryScreen(
    navController: NavHostController,
    viewModel: CategoryViewModel = hiltViewModel(),
) {

    when(val productUIState = viewModel.productUIState.collectAsState().value){
        is ProductUIState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = productUIState.message,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        ProductUIState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                CircularProgressIndicator()
            }
        }
        is ProductUIState.Success -> {

            CategoryScreenContent(
                products = productUIState.productUiSuccessState.products.collectAsState().value,
                navController,
                viewModel
            )
        }
    }

}

@Composable
fun CategoryScreenContent(
    products: List<Product>,
    navHostController: NavHostController,
    viewModel: CategoryViewModel,
    cartViewModel: CartViewModel = hiltViewModel()
    ) {

    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val cartSize by remember { derivedStateOf { cartItems.size } }

    LaunchedEffect(key1 = cartViewModel) {
        cartViewModel.cartItems.collect { items ->
            cartItems = items
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val text = stringResource(R.string.products)
            val fullText = "${viewModel.category} $text"
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp,),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navHostController.popBackStack()
                    },
                    modifier = Modifier,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )

                }


                Text(
                    text = fullText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(
                            start = 15.dp,
                        ),
                )

                BadgedBox(
                    badge = {
                        if (cartSize > 0) {
                            Badge {
                                Text(cartSize.toString())
                            }
                        }
                    }
                ) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        onClick = {
                            navHostController.navigate(Screens.Cart.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = stringResource(R.string.cart),
                        )
                    }
                }
            }


        }


        Spacer(modifier = Modifier.height(15.dp))

        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(0.8f)
                .height(300.dp),
        ){
            items(products.size){
                if(products.isEmpty()){
                    Text(
                        text = stringResource(R.string.no_product),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                ProductCard(product = products[it], navHostController)
            }
        }
    }

}
