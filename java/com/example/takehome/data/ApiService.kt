package com.example.takehome.data

import com.example.takehome.data.model.Post
import com.example.takehome.data.model.Profiles
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("profiles")
    suspend fun getProfiles(): Response<List<Profiles>>

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
}