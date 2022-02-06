package com.example.mchomework.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.mchomework.data.Reminder

@Composable
fun Home(navController: NavController) {
    Surface() {
        Scaffold(
            modifier = Modifier.padding(bottom = 30.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Payment"
                    )
                }
            }
        ) {
            Column() {
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
    val list: List<Reminder> = listOf(
        Reminder("eat food", "23.11.2020"),
        Reminder("goto sleep", "24.5.2020"),
        Reminder("drink coffee", "25.10.2020"),
        Reminder("exercise", "13.10.2020"),
        Reminder("goto work", "1.9.2020"),
        Reminder("goto school", "4.5.2020"),

        Reminder("eat food", "23.11.2020"),
        Reminder("goto sleep", "24.5.2020"),
        Reminder("drink coffee", "25.10.2020"),
        Reminder("exercise", "13.10.2020"),
        Reminder("goto work", "1.9.2020"),
        Reminder("goto school", "4.5.2020"),

        Reminder("eat food", "23.11.2020"),
        Reminder("goto sleep", "24.5.2020"),
        Reminder("drink coffee", "25.10.2020"),
        Reminder("exercise", "13.10.2020"),
        Reminder("goto work", "1.9.2020"),
        Reminder("goto school", "4.5.2020")
    )
    LazyColumn() {
        items(list) { item ->
            ReminderItem(item)
        }
    }
}

@Composable
private fun ReminderItem(
    reminder: Reminder
) {
    ConstraintLayout(modifier = Modifier.clickable {}) {
        val (divider, reminderTitle, reminderTime) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.matchParent
            }
        )
        Text(
            text = reminder.title,
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTitle) {
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                start.linkTo(parent.start, 10.dp)
            }
        )
        Text(
            text = reminder.time,
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTime) {
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                end.linkTo(parent.end, 10.dp)
            }
        )
    }
}
