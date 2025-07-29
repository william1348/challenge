package com.example.takehome.data.repository

import LocalDataSource
import android.util.Log
import com.example.takehome.data.NetworkModule
import com.example.takehome.data.model.Post

import javax.inject.Inject

class PostsRepository @Inject constructor(
    //private val localDataSource: LocalDataSource
) {

    suspend fun getPosts(): Result<List<Post>>? {
        val apiService = NetworkModule.apiService
        return try {
            val profilesResponse = apiService.getProfiles()
            if (!profilesResponse.isSuccessful) {
                return Result.failure(Exception("Error fetching profiles"))
            }
            val zeroProfileId = profilesResponse.body()?.find { it.name == "Zero" }?.id
                ?: return Result.failure(Exception("Profile Zero not found"))

            val postsResponse = apiService.getPosts()
            Log.d("response", postsResponse.body().toString())
            if (postsResponse.isSuccessful) {
                val filteredPosts = postsResponse.body()?.filter { it.profile_id == zeroProfileId }
                //localDataSource.savePosts(filteredPosts ?: emptyList())  // Save to local cache
                return Result.success(filteredPosts ?: emptyList())
            } else {
                return Result.failure(Exception("Error fetching posts"))
            }
        } catch (e: Exception) {
            // Fallback to local cache or shared preferences if network call fails
//            val localData = localDataSource.getPosts()
//            return if (localData != null) {
//                Result.success(localData)
//            } else {
//                Result.failure(e)
//            }
            Result.failure(e)
        }
    }
}