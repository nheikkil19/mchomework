package com.example.mchomework.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding

@Composable()
fun Login() {
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
            OutlinedTextField(
                value = "Username",
                onValueChange = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "Password",
                onValueChange = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

