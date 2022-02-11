package com.example.mchomework.reminder

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mchomework.login.onLoginButtonClick
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Reminder(
    navController: NavController,
    edit: Boolean
) {
    var buttonText = "Create reminder"
    val message = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    val hour = rememberSaveable { mutableStateOf("") }
    val min = rememberSaveable { mutableStateOf("") }

    if (edit) {
        buttonText = "Apply changes"
        message.value = "add later"
    }
    Surface() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Message",
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = message.value,
                onValueChange = { data -> message.value = data },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = "Day",
                    modifier = Modifier.weight(30F)
                )
                Text(
                    text = "Month",
                    modifier = Modifier.weight(30F)
                )
                Text(
                    text = "Year",
                    modifier = Modifier.weight(40F)
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
            ) {
                OutlinedTextField(
                    value = day.value,
                    onValueChange = { data -> day.value = data },
                    modifier = Modifier.weight(30F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                OutlinedTextField(
                    value = month.value,
                    onValueChange = { data -> month.value = data },
                    modifier = Modifier.weight(30F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                OutlinedTextField(
                    value = year.value,
                    onValueChange = { data -> year.value = data },
                    modifier = Modifier.weight(40F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = "Time",
                    modifier = Modifier.fillMaxWidth()
                )

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                ) {
                OutlinedTextField(
                    value = hour.value,
                    onValueChange = { data -> hour.value = data },
                    modifier = Modifier.weight(48F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                Text(
                    text = ":",
                    modifier = Modifier.weight(4F),
                )
                OutlinedTextField(
                    value = min.value,
                    onValueChange = { data -> min.value = data },
                    modifier = Modifier.weight(48F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(text = buttonText)
            }
        }
   }
}