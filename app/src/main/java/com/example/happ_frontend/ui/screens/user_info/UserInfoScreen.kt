package com.example.happ_frontend.ui.screens.user_info
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.AppViewModelProvider
import com.example.happ_frontend.ui.domain.user_info.UserInfoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    userInfoViewModel: UserInfoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val state by userInfoViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopAppBar(
            title = {
                Text("My Profile", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            },
            navigationIcon = {
                IconButton(onClick = { /* Back */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Settings */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = { /* Edit */ }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit")
                }
            }
        )

        Row {
            Image(
                painter = painterResource(R.drawable.pfp_mark_transparent),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(text = state.name, fontWeight = FontWeight.Bold)
                Text(text = "Target: Gain Weight")
            }
        }
        Column {
            Text("Full Name")
            TextField(
                value = state.name,
                onValueChange = { userInfoViewModel.updateName(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text("What is your gender")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GenderOption("Male",  true) {}
            GenderOption("Female", false) {}
            GenderOption("Other", false) {}
        }

        Column {
            Text("Current Weight")
            Slider(
                value = state.currentWeight,
                onValueChange = { userInfoViewModel.updateCurrentWeight(it) },
                valueRange = 0f..200f,
            )
        }

        Text("Goal Weight")
        Slider(
           value =  1f,
           onValueChange = { userInfoViewModel.updateGoalWeight(it) },
            valueRange = 0f..200f,
        )

        Text("Height")
        Slider(
            value = 80f,
            onValueChange = { userInfoViewModel.updateHeight(it) },
            valueRange = 0f..250f,
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0F0))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Personal Achievements")
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Friends", fontWeight = FontWeight.Bold)
            TextButton(onClick = { /* See all */ }) {
                Text("See all")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.friends.forEach { friend ->
                FriendCard(friend.name, R.drawable.pfp_cat4)
            }
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(painter = painterResource(R.drawable.pfp_mark_transparent), contentDescription = "")
            Spacer(Modifier.width(8.dp))
            Text("Friend Requests")
        }
    }
}

@Composable
fun GenderOption(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) Color(0xFFD9D0F9) else Color.Transparent
    val border = if (selected) Color.Transparent else Color.Gray

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = background),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text)
    }
}

@Composable
fun FriendCard(name: String, imageRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card (
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(80.dp)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop
            )
        }
        Text(name)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        UserInfoScreen()
    }
}
