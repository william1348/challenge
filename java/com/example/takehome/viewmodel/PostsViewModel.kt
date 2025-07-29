package com.example.takehome.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takehome.data.model.Post
import com.example.takehome.data.model.Profiles
import com.example.takehome.data.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class PostsViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel() {

//    private val _profiles = MutableStateFlow<List<Profiles>>(emptyList())
//    val profiles: MutableStateFlow<List<Profiles>> get() = _profiles

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    open val posts: MutableStateFlow<List<Post>> get() = _posts

    private val _error = MutableStateFlow<String?>(null)
    val error: MutableStateFlow<String?> get() = _error

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            val response = repository.getPosts()
            response?.fold(
                onSuccess = { data -> parseResult(data);},
                onFailure = { error ->
                    Log.e(
                        "@@@ Error fetching posts",
                        error.message ?: "Unknown error"
                    )
                 //   _error.value = error.message // uncomment this line to display error message if it exists
                }
            )
        }
    }

    private fun parseResult(data: List<Post>?) {
        Log.d("@@@", "Number of posts: ${data?.size ?: 0}")
        data?.let {
            _posts.value = it
        }
    }
}