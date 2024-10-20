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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
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
import com.semeprojects.secom.viewmodel.ProductUIState
import com.semeprojects.secom.viewmodel.ProductsViewModel

@Composable
fun ProductScreen(
    navHostController: NavHostController,
    viewModel: ProductsViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
){

    val productUIState = viewModel.productUIState.collectAsState().value

    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val cartSize by remember { derivedStateOf { cartItems.size } }

    LaunchedEffect(key1 = cartViewModel) {
        cartViewModel.cartItems.collect { items ->
            cartItems = items
        }
    }

    when(productUIState){
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

            ProductScreenContent(
                products = productUIState.productUiSuccessState.products.collectAsState().value,
                navHostController,
                viewModel,
                cartSize = cartSize
            )
        }
    }


}

@Composable
fun ProductScreenContent(
    products: List<Product>,
    navHostController: NavHostController,
    viewModel: ProductsViewModel,
    cartSize: Int
){

    val categories = listOf(
        stringResource(R.string.AllCategory),
        stringResource(R.string.electronics),
        stringResource(R.string.jewelery),
        stringResource(R.string.men_cloth),
        stringResource(R.string.women_cloth)
    )

    val selectedCategory = remember {
        mutableStateOf(categories[0])
    }

    Column(
        horizontalAlignment = Alignment.Start
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(R.string.products),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
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


        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background.copy(
                        alpha = 0.5f
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {


            categories.forEach { category ->
                Button(
                    onClick = {
                        selectedCategory.value = category
                        viewModel.getProductsByCategory(category)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(category==selectedCategory.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        contentColor = if(category==selectedCategory.value) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    )
                ) {

                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                    )
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