package com.semeprojects.secom.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.semeprojects.secom.R
import com.semeprojects.secom.ui.components.HButton
import com.semeprojects.secom.ui.components.HTextField
import com.semeprojects.secom.ui.nav_utils.Screens
import com.semeprojects.secom.viewmodel.AuthState
import com.semeprojects.secom.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(25.dp)
                    ),
                painter = painterResource(id = R.drawable.logo), contentDescription = "")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Hulugram Shop",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HTextField(
                inputState = email,
                placeholder = {
                    Text(
                        "Email",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if(authViewModel.emailError.value.isNotEmpty()){
                Text(
                    text = authViewModel.emailError.value,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HTextField(
                inputState = password,
                placeholder = {
                    Text(
                        "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {isPasswordVisible = !isPasswordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
            )
            if(authViewModel.passwordError.value.isNotEmpty()){
                Text(
                    authViewModel.passwordError.value,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HTextField(
                inputState = confirmPassword,
                placeholder = {
                    Text(
                        "Confirm Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val image = if (isConfirmPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (isConfirmPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {isConfirmPasswordVisible = !isConfirmPasswordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )
            if(authViewModel.confirmPasswordError.value.isNotEmpty()){
                Text(
                    authViewModel.confirmPasswordError.value,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            when (authState) {
                is AuthState.Loading -> {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is AuthState.Unauthenticated -> {
                    HButton(
                        text = "Sign Up",
                        click = {
                            authViewModel.validateEmail(email.value)
                            authViewModel.validatePassword(password.value)
                            authViewModel.validateConfirmPassword(password.value, confirmPassword.value)
                            if (authViewModel.emailError.value.isEmpty() &&
                                authViewModel.passwordError.value.isEmpty() &&
                                authViewModel.confirmPasswordError.value.isEmpty()) {
                                coroutineScope.launch {
                                    authViewModel.signUpWithEmailAndPassword(
                                        email.value,
                                        password.value
                                    ) { success ->
                                        if (success) {
                                            navController.navigate(Screens.Main.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        },
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    HButton(
                        text = "Continue as Guest",
                        click = {
                            navController.navigate(Screens.Main.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
                is AuthState.Authenticated -> {
                    Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT).show()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = {
                        navController.navigate(Screens.SignIn.route)
                    },
                    shape = MaterialTheme.shapes.medium
                ) {

                    Text("Sign in")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast
                            .makeText(context, "Forgot Password", Toast.LENGTH_SHORT)
                            .show()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}

@Composable
@Preview(showSystemUi = true)
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
