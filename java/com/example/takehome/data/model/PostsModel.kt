package com.example.takehome.data.model

import java.util.Date

data class Profiles(
    var id: String,
    var name: String,
    var created_at: Date
)

data class Post(
    var id: String,
    var profile_id: String,
    var title: String,
    var body: String,
    var created_at: Date
)