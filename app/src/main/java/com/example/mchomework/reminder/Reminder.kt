package com.example.mchomework.reminder


import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminder(
    navController: NavController,
    edit: Boolean,
    fusedLocationClient: FusedLocationProviderClient,
    activity: Activity,
    viewModel: ReminderViewModel = ReminderViewModel(
        fusedLocationClient = fusedLocationClient,
        location = null,
        activity = activity),
    reminderId: Int? = 0,
    markerPosition: LatLng? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val message = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    val hour = rememberSaveable { mutableStateOf("") }
    val min = rememberSaveable { mutableStateOf("") }
    val notify = rememberSaveable { mutableStateOf(true) }
    val daily = rememberSaveable { mutableStateOf(false) }
    val weekly = rememberSaveable { mutableStateOf(false) }
    val location = rememberSaveable { mutableStateOf(false) }
    val buttonText = if (!edit) stringResource(R.string.createReminder)
        else stringResource(R.string.applyChanges)
    var location_x: Double = markerPosition?.longitude ?: 200.0
    var location_y: Double = markerPosition?.latitude ?: 200.0


    if (edit) {
        LaunchedEffect(coroutineScope) {
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
                notify.value = reminder.notify
                daily.value = reminder.daily
                weekly.value = reminder.weekly
                location.value = reminder.location_bool
                if (location.value) {
                    location_x = reminder.location_x
                    location_y = reminder.location_y
                }
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
            OutlinedTextField(
                label = { Text(text = stringResource(R.string.message)) },
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
                .wrapContentHeight()
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.day)) },
                    value = day.value,
                    onValueChange = { data -> day.value = data },
                    modifier = Modifier.weight(30F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.month)) },
                    value = month.value,
                    onValueChange = { data -> month.value = data },
                    modifier = Modifier.weight(30F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.year)) },
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
                .wrapContentHeight()
                ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.hour)) },
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
                    label = { Text(text = stringResource(R.string.minutes)) },
                    value = min.value,
                    onValueChange = { data -> min.value = data },
                    modifier = Modifier.weight(48F),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(stringResource(R.string.notification), modifier = Modifier.wrapContentWidth())
                Checkbox(
                    checked = notify.value,
                    onCheckedChange = { bool -> notify.value = bool }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.wrapContentHeight(),
            ) {
                Text(stringResource(R.string.repeatDaily), modifier = Modifier.wrapContentWidth())
                Checkbox(
                    checked = daily.value,
                    enabled = notify.value and !weekly.value,
                    onCheckedChange = { bool -> daily.value = bool }
                )
                Text(stringResource(R.string.repeatWeekly), modifier = Modifier.wrapContentWidth())
                Checkbox(
                    checked = weekly.value,
                    enabled = notify.value and !daily.value,
                    onCheckedChange = { bool -> weekly.value = bool }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Location", modifier = Modifier.wrapContentWidth())
                Checkbox(
                    checked = location.value,
                    onCheckedChange = { bool -> location.value = bool }
                )
                Button(
                    onClick = { navController.navigate("selectMarker") },
                    modifier = Modifier.wrapContentSize(),
                    enabled = location.value
                ) {
                    Text(
                        text = "Pick Location"
                    )
                }
            }
            if (location.value && location_x != 200.0 && location_y != 200.0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Latitude: $location_y\nLongitude: $location_x")
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
                                        year.value,
                                    ),
                                    location_x = location_x,
                                    location_y = location_y,
                                    creation_time = Date().time,
                                    creator_id = 1,
                                    reminder_seen = false,
                                    notify = notify.value,
                                    daily = daily.value,
                                    weekly = weekly.value,
                                    location_bool = location.value
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
                                    location_x = location_x,
                                    location_y = location_y,
                                    creation_time = Date().time,
                                    creator_id = 1,
                                    reminder_seen = false,
                                    notify = notify.value,
                                    daily = daily.value,
                                    weekly = weekly.value,
                                    location_bool = location.value
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
    return if (h == "" || m == "" || d == "" || M == "" || y == "") {
        -1
    }
    else {
        val format = SimpleDateFormat("hh:mm dd.MM.yyyy", Locale("finnish"))
        val dateString = "$h:$m $d.$M.$y"
        format.parse(dateString).time
    }
}