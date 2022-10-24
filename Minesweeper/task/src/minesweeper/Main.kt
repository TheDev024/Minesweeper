package minesweeper

import java.util.Random

class Minesweeper(mines: Int, height: Int = 9, width: Int = 9) {
    private val mineField = MutableList(height) { MutableList(width) { '.' } }
    private val mines: Int
    private val height: Int
    private val width: Int
    private val random: Random

    init {
        this.mines = mines
        this.height = height
        this.width = width
        this.random = Random()
        for (mine in 1..mines) addMine()
    }

    fun printField() {
        var fieldString = ""
        for (row in mineField) {
            for (mine in row) {
                fieldString += mine
            }
            fieldString += '\n'
        }
        println(fieldString)
    }

    private fun addMine() {
        val x = random.nextInt(0, height)
        val y = random.nextInt(0, width)
        if (mineField[x][y] == 'X') addMine()
        else mineField[x][y] = 'X'
    }
}

fun main() {
    println("How many mines do you want on the field?")
    val minesweeper = Minesweeper( mines = readln().toInt() )
    minesweeper.printField()
}
