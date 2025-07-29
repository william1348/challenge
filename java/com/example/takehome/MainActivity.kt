package com.example.takehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.takehome.ui.App
import dagger.hilt.android.AndroidEntryPoint

// Using ComponentActivity assumes API > 21 otherwise use AppCompatActivity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
