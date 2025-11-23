//////////////////////////////////////////////
//                 CREDITS                  //
// Author: Enrico Malaguti Ferrari (xxyrnn) //
// GitHub: https://github.com/xxyrnn        //
// Instagram: maybe_enrico                  //
//////////////////////////////////////////////

package com.xxyrnn.snake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class GameView(context: Context) : View(context) {
    private val paint = Paint()
    private val gridSize = 60
    private lateinit var gameEngine: GameEngine
    private val handler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            gameEngine.updateGame()
            invalidate()

            if (gameEngine.isGameStarted && !gameEngine.isGameOver)
                handler.postDelayed(this, 200)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val gridWidth = w / gridSize
        val gridHeight = h / gridSize
        gameEngine = GameEngine(gridWidth, gridHeight)
        handler.post(gameRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.post(gameRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(gameRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!::gameEngine.isInitialized)
            return

        canvas.drawColor(Color.parseColor("#111111")) // background

        // draw overlay text
        paint.color = Color.YELLOW
        paint.textSize = width * 0.09f
        paint.textAlign = Paint.Align.CENTER

        if (!gameEngine.isGameStarted) {
            canvas.drawText(
                "Press anywhere to start",
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                paint
            )
        } else if (gameEngine.isGameOver) {
            canvas.drawText(
                "Game Over",
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                paint
            )
            paint.textSize = width * 0.05f
            canvas.drawText(
                "Press anywhere to restart",
                (width / 2).toFloat(),
                ((height / 2) + 50).toFloat(),
                paint
            )
        }

        // draw snake
        paint.color = Color.parseColor("#8a2be2")

        for (segment in gameEngine.snake) {
            canvas.drawCircle(
                segment.first * gridSize.toFloat(),
                segment.second * gridSize.toFloat(),
                (gridSize / 2).toFloat(),
                paint
            )
        }

        // draw food
        paint.color = Color.parseColor("#0abdc6")
        canvas.drawCircle(
            gameEngine.food.first * gridSize.toFloat(),
            gameEngine.food.second * gridSize.toFloat(),
            (gridSize / 2).toFloat(),
            paint
        )

        // draw score
        paint.color = Color.WHITE
        paint.textSize = width * 0.05f
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Score: ${gameEngine.score}", 10f, 50f, paint)
    }

    private var lastTouchX = 0f
    private var lastTouchY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (!gameEngine.isGameStarted) {
                    gameEngine.gameStart()
                    handler.post(gameRunnable)
                } else if (gameEngine.isGameOver) {
                    gameEngine.isGameStarted = false
                    gameEngine.snake.clear()
                    handler.post(gameRunnable)
                } else {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    val newDirection = if (abs(dx) > abs(dy)) {
                        if (dx > 0) Pair(1, 0) else Pair(-1, 0)
                    } else {
                        if (dy > 0) Pair(0, 1) else Pair(0, -1)
                    }

                    gameEngine.changeDirection(newDirection)
                }
            }

            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
            }
        }

        return true
    }
}