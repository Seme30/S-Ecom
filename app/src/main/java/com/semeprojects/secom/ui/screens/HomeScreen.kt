package com.semeprojects.secom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.semeprojects.secom.R
import com.semeprojects.secom.data.local.model.CartItem
import com.semeprojects.secom.ui.components.HomeCard
import com.semeprojects.secom.ui.components.ProductCard
import com.semeprojects.secom.ui.nav_utils.Screens
import com.semeprojects.secom.viewmodel.CartViewModel
import com.semeprojects.secom.viewmodel.ProductUIState
import com.semeprojects.secom.viewmodel.ProductsViewModel


data class Category(
    val name: String,
    val image: Int
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ProductsViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val products = viewModel.productUIState.collectAsState().value
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val cartSize by remember { derivedStateOf { cartItems.size } }

    LaunchedEffect(key1 = cartViewModel) {
        cartViewModel.cartItems.collect { items ->
            cartItems = items
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    350.dp
                )
        ) {
            when (products) {
                is ProductUIState.Error -> {
                    Text(
                        text = products.message,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
                    val filteredProducts =
                        products.productUiSuccessState.products.collectAsState().value.filter {
                            it.rating.rate > 4.0
                        }
                    HomeCard(
                        products = filteredProducts,
                        cartSize,
                        navController
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.trending),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 15.dp, start = 8.dp)
                )



                when (products) {
                    is ProductUIState.Error -> {
                        Text(
                            text = products.message,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
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
                        val thisProducts =
                            products.productUiSuccessState.products.collectAsState().value.filter {
                                it.rating.count > 350
                            }
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            items(thisProducts.size) {
                                ProductCard(
                                    product = thisProducts[it],
                                    navController = navController
                                )
                            }
                        }

                    }
                }
            }
        }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.categories),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 15.dp, start = 8.dp, bottom = 10.dp)
                    )



                    when(products){
                        is ProductUIState.Error -> {
                            Text(
                                text = products.message,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
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
                            val categories = products.productUiSuccessState.categories.collectAsState().value

                            val categoryImages = listOf(
                                R.drawable.electronics,
                                R.drawable.jewelery,
                                R.drawable.men_cloth,
                                R.drawable.women_cloth
                            )
                            val categoryList = categories.zip(categoryImages) { name, imageResId ->
                                Category(name, imageResId)
                            }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                            ) {
                                items(categoryList.size) { index ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .padding(10.dp)
                                            .clickable {
                                                navController.navigate(
                                                    "${Screens.Category.route}/${categoryList[index].name}"
                                                )
                                            },
                                        shape = MaterialTheme.shapes.medium,
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.1f
                                            )
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .height(80.dp)
                                                    .width(80.dp)
                                                    .clip(RoundedCornerShape(50))
                                                    .background(MaterialTheme.colorScheme.primary)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = categoryList[index].image),
                                                    contentDescription = categoryList[index].name,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                            Text(
                                                text = categoryList[index].name,
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                maxLines = 1,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                            )
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }
