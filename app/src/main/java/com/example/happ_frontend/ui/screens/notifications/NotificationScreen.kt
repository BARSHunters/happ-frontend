package com.example.happ_frontend.ui.screens.notifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.notifications.NotificationViewModel
import com.example.happ_frontend.ui.theme.HappfrontendTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class NotificationScreen :  ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappfrontendTheme {
                NotificationLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationLayout(
    notificationViewModel: NotificationViewModel = viewModel(),
    modifier: Modifier = Modifier,
){
    val notificationUIState by notificationViewModel.uiState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                launch {
                    delay(1000)
                    notificationViewModel.updateData()
                    isRefreshing = false
                }
            }
        },
        modifier = modifier
    ) {
        Column (
            modifier = modifier
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = stringResource(R.string.notifications),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            if(notificationUIState.isEmpty()){
                Text(
                    text = stringResource(R.string.notification_history_is_clear),
                    fontSize = 12.sp,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                for (notification in notificationUIState) {
                    NotificationCard(
                        type = notification.type,
                        data = notification.data,
                        date = notification.date,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    type: String,
    data: String,
    date: LocalDate,
    modifier: Modifier = Modifier
){
    val title = when (type){
        "Activity" -> stringResource(R.string.activityNotification)
        "FriendRequest" -> stringResource(R.string.friendNotification)
        "Achievement" -> stringResource(R.string.achievementNotification)
        else -> stringResource(R.string.nonValidNotification)
    }
    val  description = when (type){
        "Activity" -> stringResource(R.string.activityNotificationDescription)
        "FriendRequest" -> stringResource(R.string.friendNotificationDescription)
        "Achievement" -> stringResource(R.string.achievementNotificationDescription)
        else -> stringResource(R.string.nonValidNotificationDescription)
    }
    ElevatedCard(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)) {
        Column (modifier = modifier){
            Row (modifier = modifier.fillMaxWidth()) {
                Spacer(modifier = modifier.weight(0.1f))
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(0.1f)
                        .padding(top = 5.dp),
                    text = date.toString(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
            }
            Row (modifier =
                modifier.padding(
                    bottom = 20.dp,
                    end = 20.dp)){
                Icon(
                    Icons.Rounded.Star,
                    contentDescription = "asd",
                    modifier = modifier
                        .weight(0.3f)
                        .size(30.dp)
                        .align(Alignment.CenterVertically)

                )
                Column(
                    modifier = modifier.weight(1f),
                ) {
                    Text(
                        text = title,
                        modifier = modifier.padding(bottom = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = description,
                        fontSize = 12.sp
                    )
                    ExtraInfo(type, data, modifier = modifier)
                }
            }
        }
    }
}

@Composable
fun ExtraInfo(type: String, data: String, modifier : Modifier= Modifier){
    when (type){
        "FriendRequest" -> NewFriendExtraInfo(data, modifier)
        "Achievement" -> AchievementExtraInfo(data, modifier)
    }
}

@Composable
fun AchievementExtraInfo(achievement: String, modifier: Modifier = Modifier){
    Column (
        modifier = modifier
            .padding(end = 10.dp)
    ){
        Text(modifier = modifier,
            text = achievement,
            fontSize = 12.sp,
        )
        ElevatedButton(
            onClick = {},
            modifier = modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                stringResource(R.string.achievements),
                modifier = modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
@Composable
fun NewFriendExtraInfo(username: String, modifier: Modifier = Modifier){
    Column (
        modifier = modifier
            .padding(end = 10.dp)
    ){
        Text(modifier = modifier,
            text = username + " " + stringResource(R.string.wants_to_be_your_friend),
            fontSize = 12.sp,
        )
        ElevatedButton(
            onClick = {},
            modifier = modifier.align(Alignment.CenterHorizontally)
            ) {
            Text(
                stringResource(R.string.watch_friend_requests),
                modifier = modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun NotificationsPreview(){
    HappfrontendTheme {
        NotificationCard(
            "FriendRequest",
            "Hamza",
            LocalDate.now()
        )
    }
}