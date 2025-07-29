package com.example.takehome.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.takehome.data.model.Post
import com.example.takehome.viewmodel.PostsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takehome.R
import com.example.takehome.data.repository.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.POST
import java.util.Date

@Composable
fun App() {
    //alternatively, could use a sealed class for this if we need more complex navigation
    var selectedTab by remember { mutableStateOf("Posts") }

    // postsViewModel only necessary here for error handling, otherwise can be moved to PostsScreen
    val postsViewModel: PostsViewModel = hiltViewModel()
    val postsError by postsViewModel.error.collectAsState()

    Scaffold(
        topBar = { TopAppBarComponent(selectedTab) },
        bottomBar = { BottomNavigationBar(selectedTab, onTabSelected = { selectedTab = it }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (selectedTab == "Posts") {
                PostsScreen(viewModel = postsViewModel)
            }
            postsError?.let {
                ErrorSnackbar(errorMessage = it)
            }
        }
    }
}


@Composable
fun PostsScreen(viewModel: PostsViewModel) {
    val posts by viewModel.posts.collectAsState()
    if(posts.isEmpty()){
        Text("Loading...") // TODO - more robust solution using sealed class to manage state in VM
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        items(posts) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    //var isBodyVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
//            isBodyVisible = !isBodyVisible
            },
        shape = RoundedCornerShape(2.dp),

        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.nice_blue)
        )
    ) {
        Text(
            text = post.title,
            modifier = Modifier.padding(20.dp),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
        )
//        if (isBodyVisible) {
//            Text(
//                text = post.body,
//                style = TextStyle(
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Normal,
//                    color = Color.White.copy(alpha = 0.8f)
//                ),
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
    }
}


@Composable
fun ErrorSnackbar(errorMessage: String) {
    Snackbar(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        content = {
            Text(text = errorMessage, color = Color.White, fontSize = 16.sp)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(selectedTab: String) {
    TopAppBar(title = { Text(selectedTab) })
}


@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == "Posts",
            onClick = { onTabSelected("Posts") },
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Posts"
                )
            },
            label = { Text("Posts") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id = R.color.dark_blue),
                unselectedIconColor = colorResource(id = R.color.somewhat_green),
                selectedTextColor = colorResource(id = R.color.dark_blue),
                unselectedTextColor = Color.Gray
            )
        )
//        NavigationBarItem(
//            selected = selectedTab == "Profile",
//            onClick = { onTabSelected("Profile") },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Person, // Replace with the appropriate icon
//                    contentDescription = "Profile"
//                )
//            },
//            label = { Text("Profile") }
//        )
    }
}


/* PREVIEWS AND MOCK DATA */

class MockPostsViewModel : PostsViewModel(PostsRepository()) {
    override val posts: MutableStateFlow<List<Post>> = MutableStateFlow(
        listOf(
            Post(
                id = "403",
                title = "Here is a cool post",
                body = "This is the body of the post",
                created_at = Date("12/08/1991"),
                profile_id = "0"
            ),
            Post(
                id = "404",
                title = "Here is another cool post with a really really long title. really long title. really really really really really long title.",
                body = "This is the body of the post",
                created_at = Date("12/08/1991"),
                profile_id = "-1"
            ),
            Post(
                id = "405",
                title = "Here is a cool post",
                body = "Here is a ton of body text. way too much body text. This is the body of the post, Here is a ton of body text. way too much body text. This is the body of the post Here is a ton of body text. way too much body text. This is the body of the post Here is a ton of body text. way too much body text. This is the body of the post",
                created_at = Date("12/08/1991"),
                profile_id = "101"
            )
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PostsScreenPreview() {
    val mockViewModel = remember { MockPostsViewModel() }
    PostsScreen(viewModel = mockViewModel)
}


@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    PostItem(
        post = Post(
            id = "40",
            title = "Here is a cool post",
            body = "This is the body of the post",
            created_at = Date("12/08/1991"),
            profile_id = "10"
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PostsErrorPreview() {
    ErrorSnackbar(errorMessage = "Error fetching posts")
}