package com.example.mchomework.home

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mchomework.R
import com.example.mchomework.data.entity.Reminder
import com.example.mchomework.notification.createNotificationChannel
import com.example.mchomework.notification.notifyReminder
import com.example.mchomework.reminder.ReminderViewModel
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Home(
    navController: NavController,
    sharedPref: SharedPreferences
) {
    val viewModel: ReminderViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Surface() {
        Scaffold(
            modifier = Modifier.padding(bottom = 30.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("addReminder") },
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.newPayment)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { logOutClick(
                        navController = navController,
                        sharedPref = sharedPref
                    ) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.logOut))
                }
                reminderList(
                    list = viewState.reminders,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun reminderList(
    list: List<Reminder>,
    navController: NavController,
    viewModel: ReminderViewModel
) {
    LazyColumn() {
        items(list) { item ->
            ReminderItem(
                reminder = item,
                navController = navController,
                viewModel = viewModel
            )
        }
    }

}

@Composable
private fun ReminderItem(
    reminder: Reminder,
    navController: NavController,
    viewModel: ReminderViewModel
) {
    ConstraintLayout(modifier = Modifier.clickable { navController.navigate("editReminder${reminder.id}") }) {
        val (divider, reminderTitle, reminderTime, delButton) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.matchParent
            }
        )
        Text(
            text = reminder.message,
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTitle) {
                top.linkTo(parent.top, 10.dp)
                bottom.linkTo(reminderTime.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
            }
        )
        Text(
            text = reminder.reminder_time.toDateString(),
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTime) {
                top.linkTo(reminderTitle.bottom, 10.dp)
                bottom.linkTo(parent.bottom, 10.dp)
                start.linkTo(parent.start, 10.dp)
            }
        )
        Button(
            onClick = { runBlocking { viewModel.deleteReminder(reminder)
            } },
            modifier = Modifier.constrainAs(delButton) {
                top.linkTo(parent.top, 10.dp)
                bottom.linkTo(parent.bottom, 10.dp)
                end.linkTo(parent.end, 10.dp)
            }
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
        }
    }
}

fun logOutClick(
    navController: NavController,
    sharedPref: SharedPreferences
) {
    with(sharedPref.edit()) {
        putBoolean("loggedIn", false)
        apply()
    }
    navController.navigate("login")
}

private fun Long.toDateString(): String {
    return SimpleDateFormat("hh:mm dd.MM.yyyy", Locale("finnish")).format(this)
}
