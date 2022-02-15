package com.example.mchomework.login

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mchomework.R
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Login(
    sharedPref: SharedPreferences,
    navController: NavController
    ) {
    val username = rememberSaveable{ mutableStateOf("") }
    val password = rememberSaveable{ mutableStateOf("") }

    Surface( modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountBox,
                contentDescription = stringResource(R.string.account),
                modifier = Modifier.size(180.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = stringResource(R.string.username), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = stringResource(R.string.password), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = { onLoginButtonClick(
                    sharedPref,
                    username.value,
                    password.value,
                    navController
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(text = stringResource(R.string.login))
            }
        }
    }
}

fun onLoginButtonClick(
    sharedPref: SharedPreferences,
    username: String,
    password: String,
    navController: NavController
) {
    if ( username == sharedPref.getString("username", null) &&
         password == sharedPref.getString("password", null) ) {
        with(sharedPref.edit()) {
            putBoolean("loggedIn", true)
            apply()
        }
        navController.navigate("home")
    }
}