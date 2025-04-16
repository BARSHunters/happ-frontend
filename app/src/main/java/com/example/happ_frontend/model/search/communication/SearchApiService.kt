package com.example.happ_frontend.model.search.communication

import com.example.happ_frontend.model.search.data.SearchUserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchApiService {
    @GET("searchByName/{query}")
    suspend fun getSearchUsers(@Path("query") query: String) : Response<List<SearchUserDto>>
}