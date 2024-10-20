package com.semeprojects.secom.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.semeprojects.secom.R
import com.semeprojects.secom.data.network.model.Product
import com.semeprojects.secom.ui.nav_utils.Screens

data class ImageData(
    val imageUrl: String,
    val title: String,
    val price: Double
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeCard(
    products: List<Product>,
    cartSize: Int,
    navHostController: NavHostController
){

    var isSearchVisible by remember { mutableStateOf(false) }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray, RoundedCornerShape(20.dp))
    ) {

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            products.size
        }
        if(products.isEmpty()){
            Text(
                text = stringResource(R.string.no_product),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalPager(state = pagerState) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(products[page].image),
                    contentDescription = products[page].description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                HomeCardContent(
                    title = products[page].title,
                    price = products[page].price,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

        }

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            pagerState = pagerState,
            pageCount = products.size,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = Color.Gray,
            indicatorShape = RoundedCornerShape(50f)
        )

        Text(
            text = stringResource(R.string.recommendations),
            style = MaterialTheme.typography.displaySmall,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    start = 10.dp,
                    top = 100.dp
                )
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
//            when(isSearchVisible){
//                true -> {
//                    HTextField(
//                        modifier = Modifier.weight(0.8f),
//                        leadingIcon = {
//                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
//                        },
//                        placeholder = {
//                            Text(
//                             text = stringResource(R.string.search_hint),
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        },
//                        keyboardActions = KeyboardActions(
//                            onDone = {
//                                isSearchVisible = !isSearchVisible
//                            }
//                        ),
//                        trailingIcon = {
//                            IconButton(
//                                onClick = {
//                                    isSearchVisible = !isSearchVisible
//                                }
//                            ) {
//                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Back")
//                            }
//                        }
//                    )
//                }
//                false -> {
//                    IconButton (
//                        colors = IconButtonDefaults.iconButtonColors(
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            contentColor = MaterialTheme.colorScheme.onPrimary,
//                        ),
//                        onClick = {
//                            isSearchVisible = !isSearchVisible
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Search,
//                            contentDescription = stringResource(R.string.search),
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))

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
}

@Composable
fun HomeCardContent(
    title: String,
    price: Double,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxHeight(0.3f)
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            "$${price}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }


}
