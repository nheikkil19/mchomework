package com.example.mchomework.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.mchomework.Graph.database
import com.example.mchomework.Graph.reminderRepository
import com.example.mchomework.data.MyDatabase
import com.example.mchomework.data.Reminder
import com.example.mchomework.data.ReminderDao
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Home(navController: NavController) {
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
                        contentDescription = "New Payment"
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
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Log Out")
                }
                reminderList()
            }
        }
    }
}

@Composable
fun reminderList() {
    val coroutineScope = rememberCoroutineScope()
    val list: List<Reminder> = listOf(
        Reminder(
            message = "eat food",
            reminder_time = Date().time,
            creation_time = Date().time,
            creator_id = 1,
            reminder_seen = false
        )
    )

    for (item: Reminder in list) {
        coroutineScope.launch { reminderRepository.addReminder(item) }

    }
    /*
    LazyColumn() {
        items(list) { item ->
            ReminderItem(item)
        }
    }

     */
}

@Composable
private fun ReminderItem(
    reminder: Reminder
) {
    ConstraintLayout(modifier = Modifier.clickable { /*editReminder(reminder)*/ }) {
        val (divider, reminderTitle, reminderTime) = createRefs()
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
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                start.linkTo(parent.start, 10.dp)
            }
        )
        Text(
            text = reminder.reminder_time.toDateString(),
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTime) {
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                end.linkTo(parent.end, 10.dp)
            }
        )
    }
}

private fun Long.toDateString(): String {
    return SimpleDateFormat("dd.MM.yyyy").format(this)
}