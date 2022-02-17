package com.example.mchomework.reminder


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mchomework.R
import com.example.mchomework.notification.setNotificationAtTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminder(
    navController: NavController,
    edit: Boolean,
    viewModel: ReminderViewModel = viewModel(),
    reminderId: Int? = 0
) {
    val coroutineScope = rememberCoroutineScope()
    val message = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    val hour = rememberSaveable { mutableStateOf("") }
    val min = rememberSaveable { mutableStateOf("") }
    val buttonText = if (!edit) stringResource(R.string.createReminder)
    else stringResource(R.string.applyChanges)

    if (edit) {
        coroutineScope.launch {
            val reminder = reminderId?.let { viewModel.getReminder(it) }
            val cal = Calendar.getInstance()
            if (reminder != null) {
                cal.time = Date(reminder.reminder_time)
                message.value = reminder.message
                hour.value = cal.get((Calendar.HOUR_OF_DAY)).toString()
                min.value = cal.get((Calendar.MINUTE)).toString()
                day.value = cal.get((Calendar.DAY_OF_MONTH)).toString()
                month.value = cal.get((Calendar.MONTH)).toString()
                year.value = cal.get((Calendar.YEAR)).toString()
            }
        }
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
                text = stringResource(R.string.message),
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
                    text = stringResource(R.string.day),
                    modifier = Modifier.weight(30F)
                )
                Text(
                    text = stringResource(R.string.month),
                    modifier = Modifier.weight(30F)
                )
                Text(
                    text = stringResource(R.string.year),
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
                    text = stringResource(R.string.time),
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
                onClick = {
                    coroutineScope.launch {
                        if (!edit) {
                            viewModel.saveReminder(
                                com.example.mchomework.data.entity.Reminder(
                                    message = message.value,
                                    reminder_time = dateToLong(
                                        hour.value,
                                        min.value,
                                        day.value,
                                        month.value,
                                        year.value
                                    ),
                                    location_x = 0.0,
                                    location_y = 0.0,
                                    creation_time = Date().time,
                                    creator_id = 1,
                                    reminder_seen = false
                                )
                            )
                        }
                        else {
                            reminderId?.let {
                                com.example.mchomework.data.entity.Reminder(
                                    id = it,
                                    message = message.value,
                                    reminder_time = dateToLong(
                                        hour.value,
                                        min.value,
                                        day.value,
                                        month.value,
                                        year.value
                                    ),
                                    location_x = 0.0,
                                    location_y = 0.0,
                                    creation_time = Date().time,
                                    creator_id = 1,
                                    reminder_seen = false
                                )
                            }?.let {
                                viewModel.updateReminder(
                                    it
                                )
                            }
                        }
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(text = buttonText)
            }
        }
   }
}

fun dateToLong( h: String, m: String, d: String, M: String, y: String): Long {
    val format = SimpleDateFormat("hh:mm dd.MM.yyyy", Locale.getDefault())
    val dateString = "$h:$m $d.$M.$y"
    return format.parse(dateString).time
}