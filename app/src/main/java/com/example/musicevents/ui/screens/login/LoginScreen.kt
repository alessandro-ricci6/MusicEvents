package com.example.musicevents.ui.screens.login

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.musicevents.R
import com.example.musicevents.ui.MusicEventsRoute
import com.example.musicevents.ui.screens.login.LoginViewModel

@Composable
fun LoginScreen(
    navHostController: NavHostController
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        LoginDiv(navHostController)
        RegisterScreen()

    }
}

@Composable
fun LoginDiv(
    navController: NavHostController,
){
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }
    val icon = if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                passwordVisible = !passwordVisible
            },
        ) {
            Icon(
                ImageVector.vectorResource(icon),
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    Text(text = "Login", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    TextField(value = name,
        onValueChange = { name = it },
        modifier = Modifier
            .padding(top = 30.dp),
        label = {Text(text = "Name")})
    TextField(
        value = password,
        modifier = Modifier
            .padding(30.dp),
        trailingIcon = trailingIconView,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
    Button(onClick = {
        navController.navigate(MusicEventsRoute.Home.route)
        resultText = if(LoginViewModel().onLoginClick(name, password))"Hello $name" else "Try again"
    }) {
        Text(text = "Login")
    }
    Text(text = resultText)
}

@Composable
fun RegisterScreen(){
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }
    val icon = if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                passwordVisible = !passwordVisible
            },
        ) {
            Icon(
                ImageVector.vectorResource(icon),
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    Text(text = "Register", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    TextField(
        value = email,
        onValueChange = { email = it },
        modifier = Modifier
            .padding(top = 30.dp),
        label = { Text(text = "Email") })
    TextField(value = name,
        onValueChange = { name = it },
        modifier = Modifier
            .padding(top = 30.dp),
        label = {Text(text = "Name")})
    TextField(
        value = password,
        modifier = Modifier
            .padding(30.dp),
        trailingIcon = trailingIconView,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
    Button(onClick = {
        resultText = if(LoginViewModel().onRegisterClick(name, password, email)) "Hello $email" else "Try again"
    }) {
        Text(text = "Register")
    }
    Text(text = resultText)
}
