package com.example.happ_frontend.ui.screens.user_info
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.model.user_info.request.Gender
import com.example.happ_frontend.model.user_info.request.WeightDesire
import com.example.happ_frontend.ui.AppViewModelProvider
import com.example.happ_frontend.ui.domain.user_info.UserInfoViewModel
import com.example.happ_frontend.ui.screens.home.ProfilePicture
import kotlin.math.round
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    userInfoViewModel: UserInfoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit = {},
    onExit: () -> Unit = {}
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
                IconButton(onClick = onGoBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onExit) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit")
                }
            }
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            ProfilePicture(
                username = state.username,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(text = state.username, fontWeight = FontWeight.Bold)
                Text(text = "Target: Gain Weight")
                Text(text = "Birth Date: " + state.birthDate)
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
            GenderOptions(
                selectedGender = state.gender,
                onGenderSelected = { userInfoViewModel.updateGender(it) }
            )
        }

        Column {
            Text("Current Weight: ${state.currentWeight}")
            Slider(
                value = state.currentWeight,
                onValueChange = { userInfoViewModel.updateCurrentWeight( round(it * 10) / 10) },
                valueRange = 50f..300f,
                onValueChangeFinished = { userInfoViewModel.save() }
            )
        }

        WeightDesireOptions(
            selectedDesire = state.goalWeight,
            onDesireSelected = { userInfoViewModel.updateGoalWeightDesire(it) }
        )

        Text("Height: ${state.height.roundToInt()}")
        Slider(
            value = state.height,
            onValueChange = { userInfoViewModel.updateHeight(it.roundToInt().toFloat()); },
            valueRange = 50f..300f,
            onValueChangeFinished = { userInfoViewModel.save() }
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0F0))
        ) {
//            Row(
//                modifier = Modifier.padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Default.Favorite, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Personal Achievements")
//            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Friends", fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.friends.forEach { friend ->
                FriendCard(friend)
            }
        }

//        OutlinedButton(
//            onClick = { },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Image(painter = painterResource(R.drawable.pfp_mark_transparent), contentDescription = "")
//            Spacer(Modifier.width(8.dp))
//            Text("Friend Requests")
//        }
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
fun GenderOptions(selectedGender: Gender, onGenderSelected: (Gender) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Gender.values().forEach { gender ->
            val isSelected = gender == selectedGender
            GenderOption(
                text = gender.name.lowercase().replaceFirstChar { it.uppercase() },
                selected = isSelected,
                onClick = { onGenderSelected(gender) }
            )
        }
    }
}

@Composable
fun FriendCard(username: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card (
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(80.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProfilePicture(
                    username = username
                )
            }
        }
        Text(username)
    }
}

@Composable
fun WeightDesireOptions(selectedDesire: WeightDesire, onDesireSelected: (WeightDesire) -> Unit) {
    Column {
        Text("Goal Weight")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeightDesire.values().forEach { desire ->
                val isSelected = desire == selectedDesire
                OutlinedButton(
                    onClick = { onDesireSelected(desire) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFD9D0F9) else Color.Transparent
                    ),
                    border = BorderStroke(1.dp, if (isSelected) Color.Transparent else Color.Gray),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(desire.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        UserInfoScreen()
    }
}
