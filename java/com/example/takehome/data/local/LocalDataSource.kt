import com.example.takehome.data.model.Post

interface LocalDataSource {
    suspend fun savePosts(list : List<Post>)
    suspend fun getPosts() : List<Post>
}

// TODO - implement LocalDataSource