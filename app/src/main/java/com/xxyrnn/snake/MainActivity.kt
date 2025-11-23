//////////////////////////////////////////////
//                 CREDITS                  //
// Author: Enrico Malaguti Ferrari (xxyrnn) //
// GitHub: https://github.com/xxyrnn        //
// Instagram: maybe_enrico                  //
//////////////////////////////////////////////

package com.xxyrnn.snake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xxyrnn.snake.ui.theme.SnakeTheme
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val controller = window.insetsController

        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            // fallback
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
        setContent {
            SnakeTheme {
                GameScreen()
            }
        }
    }

    @Composable
    fun GameScreen() {
        AndroidView(
            factory = { context -> GameView(context) },
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}
