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
    private val gridSize = 40
    private lateinit var gameEngine: GameEngine
    private val handler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            gameEngine.updateGame()
            invalidate()

            if (!gameEngine.isGameOver)
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

        canvas.drawColor(Color.BLACK) // background

        // draw overlay text
        paint.color = Color.YELLOW
        paint.textSize = 100f
        paint.textAlign = Paint.Align.CENTER

        if (!gameEngine.isGameStarted) {
            canvas.drawText(
                "Press to start",
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
            paint.textSize = 50f
            canvas.drawText(
                "Press to restart",
                (width / 2).toFloat(),
                ((height / 2) + 50).toFloat(),
                paint
            )
        }

        // draw snake
        paint.color = Color.GREEN

        for (segment in gameEngine.snake) {
            canvas.drawRect(
                segment.first * gridSize.toFloat(),
                segment.second * gridSize.toFloat(),
                (segment.first + 1) * gridSize.toFloat(),
                (segment.second + 1) * gridSize.toFloat(),
                paint
            )
        }

        // draw food
        paint.color = Color.RED
        canvas.drawRect(
            gameEngine.food.first * gridSize.toFloat(),
            gameEngine.food.second * gridSize.toFloat(),
            (gameEngine.food.first + 1) * gridSize.toFloat(),
            (gameEngine.food.second + 1) * gridSize.toFloat(),
            paint
        )

        // draw score
        paint.color = Color.WHITE
        paint.textSize = 50f
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