package com.example.mchomework.login

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@Composable()
fun Login(navController: NavController) {
    var username = rememberSaveable{ mutableStateOf("") }
    var password = rememberSaveable{ mutableStateOf("") }

    Surface( modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountBox,
                contentDescription = "Account",
                modifier = Modifier.size(180.dp),
                tint = Color.Cyan
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = "Username", modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = "Password", modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Button(
                onClick = { /*goto homescreen*/},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

        }
    }
}

