package com.example.happ_frontend.ui.domain.search

import androidx.lifecycle.ViewModel
import com.example.happ_frontend.model.search.data.SearchUserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    private var searchUIState = SearchScreenUIState()
    private val _uiState = MutableStateFlow(searchUIState)
    val uiState: StateFlow<SearchScreenUIState> = _uiState.asStateFlow()

    fun searchForUser(search: String) {
        val newData = emptyList<SearchUserDto>()
        _uiState.update {
            SearchScreenUIState(
                search,
                newData
            )
        }
    }
}


data class SearchScreenUIState(
    var searchString : String = "",
    var searchUsers : List<SearchUserDto> = emptyList()
)