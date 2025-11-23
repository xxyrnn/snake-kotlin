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
import androidx.compose.ui.viewinterop.AndroidView
import com.xxyrnn.snake.ui.theme.SnakeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            modifier = Modifier.fillMaxSize()
        )
    }
}
