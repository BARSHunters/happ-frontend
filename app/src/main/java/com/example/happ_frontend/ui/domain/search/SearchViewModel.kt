package com.example.happ_frontend.ui.domain.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.search.communication.SearchRetrofitInstance
import com.example.happ_frontend.model.search.data.SearchUserDto
import com.example.happ_frontend.ui.navigation.NavigateToLoginUIEvent
import com.example.happ_frontend.ui.navigation.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var searchUIState = SearchScreenUIState()
    private val _uiState = MutableStateFlow(searchUIState)
    val uiState: StateFlow<SearchScreenUIState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun searchForUser(search: String) {
        searchUIState.searchString = search
        if(search == ""){
            _uiState.update {
                SearchScreenUIState(
                    searchString = searchUIState.searchString,
                    searchUsers = emptyList(),
                    statusOfScreen = SearchScreenStatus.NO_DATA
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                SearchScreenUIState(
                    searchString = searchUIState.searchString,
                    statusOfScreen = SearchScreenStatus.LOADING
                )
            }
            try {
                val fetchedUsers = SearchRetrofitInstance.searchApiService.getSearchUsers(search)
                if (fetchedUsers.code() == 401){
                    _eventFlow.emit(NavigateToLoginUIEvent)
                    return@launch
                } else if(fetchedUsers.isSuccessful) {
                    if(fetchedUsers.body() != null && fetchedUsers.body()?.size != 0){
                        _uiState.update {
                            SearchScreenUIState(
                                searchString = searchUIState.searchString,
                                searchUsers = fetchedUsers.body()!!,
                                statusOfScreen = SearchScreenStatus.SHOW_DATA
                            )
                        }
                    } else {
                        _uiState.update {
                            SearchScreenUIState(
                                searchString = searchUIState.searchString,
                                searchUsers = emptyList(),
                                statusOfScreen = SearchScreenStatus.NO_DATA
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        SearchScreenUIState(
                            searchString = searchUIState.searchString,
                            statusOfScreen = SearchScreenStatus.SERVER_ERROR
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    SearchScreenUIState(
                        searchString = searchUIState.searchString,
                        statusOfScreen = SearchScreenStatus.SERVER_ERROR
                    )
                }
            }
        }
    }
}

enum class SearchScreenStatus {
    LOADING,
    SERVER_ERROR,
    NO_DATA,
    SHOW_DATA
}

data class SearchScreenUIState(
    var searchString : String = "",
    var statusOfScreen : SearchScreenStatus = SearchScreenStatus.NO_DATA,
    var searchUsers : List<SearchUserDto> = emptyList()
)