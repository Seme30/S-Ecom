package com.semeprojects.secom.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.semeprojects.secom.data.network.model.User
import com.semeprojects.secom.data.preferences.Theme
import com.semeprojects.secom.ui.nav_utils.Screens
import com.semeprojects.secom.viewmodel.AuthViewModel
import com.semeprojects.secom.viewmodel.ProfileUIState
import com.semeprojects.secom.viewmodel.ProfileViewModel
import com.semeprojects.secom.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by profileViewModel.profileUiState.collectAsState()
    val currentTheme by themeViewModel.theme.collectAsState()

    when (val state = uiState) {
        is ProfileUIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileUIState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
        is ProfileUIState.Success -> {
            val user = state.user
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    UserInfoSection(name = user.displayName ?: "Guest User", email = user.email ?: "")
                }
                item {
                    SettingsSection(navController, authViewModel, themeViewModel, currentTheme, user)
                }
            }
        }
    }
}

@Composable
fun UserInfoSection(name: String, email: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsSection(navController: NavHostController, profileViewModel: AuthViewModel,     themeViewModel: ThemeViewModel,
                    currentTheme: Theme,
                    user: User
                    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ThemeToggleItem(themeViewModel, currentTheme)

            SettingsItem(
                icon = Icons.Default.ShoppingCart,
                title = "Order History",
                onClick = {
                    Toast.makeText(navController.context, "Order History", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.Favorite,
                title = "Wishlist",
                onClick = {
                    Toast.makeText(navController.context, "Wishlist", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.CreditCard,
                title = "Payment Methods",
                onClick = {
                    Toast.makeText(navController.context, "Payment Methods", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.LocationOn,
                title = "Shipping Addresses",
                onClick = {
                    Toast.makeText(navController.context, "Shipping Addresses", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                onClick = {
                    Toast.makeText(navController.context, "Notifications", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.Security,
                title = "Privacy and Security",
                onClick = {
                    Toast.makeText(navController.context, "Privacy and Security", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.Help,
                title = "Help and Support",
                onClick = {
                    Toast.makeText(navController.context, "Help and Support", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsItem(
                icon = Icons.Default.ExitToApp,
                title = if(user.displayName == "Guest User") "Sign In" else "Sign Out",
                onClick = {
                    profileViewModel.logout()
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(Screens.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ThemeToggleItem(themeViewModel: ThemeViewModel, currentTheme: Theme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                themeViewModel.setTheme(if (currentTheme == Theme.DARK) Theme.LIGHT else Theme.DARK)
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (currentTheme == Theme.DARK) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = if (currentTheme == Theme.DARK) "Dark Mode" else "Light Mode", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = currentTheme == Theme.DARK,
            onCheckedChange = {
                themeViewModel.setTheme(if (it) Theme.DARK else Theme.LIGHT)
            }
        )
    }
}