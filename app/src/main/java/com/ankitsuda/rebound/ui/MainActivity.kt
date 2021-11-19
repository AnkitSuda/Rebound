package com.ankitsuda.rebound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.view.WindowCompat
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.ui.main.MainScreen
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var prefStorage: PrefStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes content draw under status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme

                    Surface(color = MaterialTheme.colors.background) {
                        MainScreen()
                    }
                }

        }
    }
}