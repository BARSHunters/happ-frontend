package com.example.happ_frontend.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.R
import com.example.happ_frontend.model.search.data.SearchUserDto
import com.example.happ_frontend.ui.AppViewModelProvider
import com.example.happ_frontend.ui.domain.search.SearchScreenStatus
import com.example.happ_frontend.ui.domain.search.SearchViewModel
import com.example.happ_frontend.ui.navigation.NavigateToLoginUIEvent
import com.example.happ_frontend.ui.screens.home.chooseImageForUsername

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onUnAuth: () -> Unit,
    searchViewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        searchViewModel.eventFlow.collect { event ->
            when(event){
                is NavigateToLoginUIEvent -> {
                    onUnAuth()
                }
            }
        }
    }
    val searchUIState by searchViewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    TextField(
                        value = searchUIState.searchString,
                        onValueChange = {
                            searchViewModel.searchForUser(it)
                        },
                        placeholder = { Text(stringResource(R.string.UserSearchTop)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        )
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            }
        )
        if(searchUIState.statusOfScreen == SearchScreenStatus.NO_DATA || searchUIState.statusOfScreen == SearchScreenStatus.SERVER_ERROR) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(
                    when(searchUIState.statusOfScreen){
                        SearchScreenStatus.NO_DATA -> R.string.no_users_found
                        else -> R.string.server_unavailable
                    }
                ),
                textAlign = TextAlign.Center
            )
        } else if (searchUIState.statusOfScreen == SearchScreenStatus.LOADING){
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else if (searchUIState.statusOfScreen == SearchScreenStatus.SHOW_DATA){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            ) {
                items(searchUIState.searchUsers) { user ->
                    UserListItem(user, onUserClick)
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: SearchUserDto, onUserClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onUserClick(user.username) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Image(
                painter = painterResource(id = chooseImageForUsername(user.username)),
                contentDescription = "Search User Image",
                contentScale = ContentScale.Crop, // or Fit / FillBounds / Inside
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = user.name)
            Text(text = "@${user.username}", color = Color.Gray)
        }
    }
}

