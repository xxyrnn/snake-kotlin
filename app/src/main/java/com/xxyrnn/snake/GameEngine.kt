//////////////////////////////////////////////
//                 CREDITS                  //
// Author: Enrico Malaguti Ferrari (xxyrnn) //
// GitHub: https://github.com/xxyrnn        //
// Instagram: maybe_enrico                  //
//////////////////////////////////////////////

package com.xxyrnn.snake

class GameEngine(private val gridWidth: Int, private val gridHeight: Int) {
    val snake = mutableListOf<Pair<Int, Int>>()
    var food = Pair(0, 0)
    var direction = Pair(1, 0)
    var score = 0
    var isGameStarted = true
    var isGameOver = false

    init {
        gameStart()
    }

    fun gameStart() {
        val centerX = gridWidth / 2
        val centerY = gridHeight / 2
        snake.add(Pair(centerX, centerY))
        direction = Pair(0, -1)
        score = 0
        isGameOver = false
        isGameStarted = true
        generateFood()
    }

    fun updateGame() {
        if (isGameOver)
            return

        val head = snake.first()
        val newHead = Pair(head.first + direction.first, head.second + direction.second)
        snake.add(0, newHead)

        // check for collision with food
        if (newHead == food) {
            score++
            generateFood()
        } else {
            snake.removeAt(snake.size - 1)
        }

        // check for collision with wall or self
        checkCollisions(newHead)
    }

    private fun generateFood() {
        do {
            food = Pair((1 until gridWidth).random(), (1 until gridHeight).random())
        } while (food in snake)
    }

    private fun checkCollisions(newHead: Pair<Int, Int>) {
        val frontX = when {
            direction.first > 0 -> newHead.first + 1
            else -> newHead.first
        }
        val frontY = when {
            direction.second > 0 -> newHead.second + 1
            else -> newHead.second
        }

        if (frontX < 0 || frontX > gridWidth ||
            frontY < 0 || frontY > gridHeight ||
            newHead in snake.drop(1))
            isGameOver = true
    }

    fun changeDirection(newDirection: Pair<Int, Int>) {
        // prevent going in the same direction as body
        if (newDirection != Pair(-direction.first, -direction.second))
            direction = newDirection
    }
}