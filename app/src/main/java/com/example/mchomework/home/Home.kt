package com.example.mchomework.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.mchomework.data.Reminder

@Composable
fun Home(navController: NavController) {
    reminderList()
}

@Composable
fun reminderList() {
    val list: List<Reminder> = listOf(
        Reminder("eat food", 123),
        Reminder("goto sleep", 234),
        Reminder("drink coffee", 345),
        Reminder("exercise", 456),
        Reminder("goto work", 567),
        Reminder("goto school", 678),

        Reminder("eat food", 123),
        Reminder("goto sleep", 234),
        Reminder("drink coffee", 345),
        Reminder("exercise", 456),
        Reminder("goto work", 567),
        Reminder("goto school", 678),

        Reminder("eat food", 123),
        Reminder("goto sleep", 234),
        Reminder("drink coffee", 345),
        Reminder("exercise", 456),
        Reminder("goto work", 567),
        Reminder("goto school", 678),

        Reminder("eat food", 123),
        Reminder("goto sleep", 234),
        Reminder("drink coffee", 345),
        Reminder("exercise", 456),
        Reminder("goto work", 567),
        Reminder("goto school", 678),
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
//                linkTo(
//                    start = parent.start,
//                    end = reminderTime.end,
//                    startMargin = 20.dp,
//                    endMargin = 10.dp,
//                    bias = 0f
//                )
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                start.linkTo(parent.start, 10.dp)
            }
        )
        Text(
            text = reminder.time.toString(),
            maxLines = 1,
            modifier = Modifier.constrainAs(reminderTime) {
                top.linkTo(parent.top, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
                end.linkTo(parent.end, 10.dp)
            }
        )
    }
}

