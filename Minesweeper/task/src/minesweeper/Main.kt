package minesweeper

import java.util.*

val scanner = Scanner(System.`in`)

class Cell {
    var isOpen: Boolean = true
    var isMine: Boolean = false
    var mineCount: Int = 0
    var isMarked: Boolean = false
}

class Minesweeper(mines: Int, height: Int = 9, width: Int = 9) {
    private val mineField = MutableList(height) { MutableList(width) { Cell() } } // empty minefield
    private val mines: Int // number of mines
    private val height: Int // height of the field
    private val width: Int // width of the field
    private val random: Random

    init {
        this.mines = mines
        this.height = height
        this.width = width
        this.random = Random()
        // initialize the mine filed with the given number of mines
        for (mine in 1..mines) addMine()
        countMines()
    }

    fun startGame() {
        printField()
        while (true) {
            println("Set/delete mine marks (x and y coordinates):")
            val x = scanner.nextInt() - 1
            val y = scanner.nextInt() - 1
            if (mineField[y][x].mineCount > 0) {
                println("There is a number here!")
                continue
            } else mineField[y][x].isMarked = !mineField[y][x].isMarked
            printField()
            if (win()) break
        }
        println("Congratulations! You found all the mines!")
    }

    /**
     * Check for all cells in mine filed, if all mines are marked and all empty cells are unmarked the player wins
     * @return true if the player wins, false otherwise
     */
    private fun win(): Boolean {
        for (row in mineField)
            for (cell in row)
                if (cell.isMine != cell.isMarked)
                    return false
        return true
    }

    /**
     * Count mines around empty cells
     */
    private fun countMines() {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (!mineField[i][j].isMine) { // check if this cell is empty
                    for (row in i - 1..i + 1) {
                        for (col in j - 1..j + 1) {
                            // 1. Check if it isn't this cell 2. Check if the index not out of the range
                            if ((row != i || col != j) && row in 0 until height && col in 0 until width) {
                                if (mineField[row][col].isMine) mineField[i][j].mineCount++
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Prints the field to the console
     */
    private fun printField() {
        var fieldString = " |"
        for (i in 1..width) fieldString += i
        fieldString += '|'
        fieldString += '\n'
        fieldString += "—|"
        repeat(width) {
            fieldString += '—'
        }
        fieldString += '|'
        fieldString += '\n'
        var i = 1
        for (row in mineField) {
            fieldString += "${i++}|"
            for (cell in row) {
                /*if (cell.isOpen) {
                    if (cell.isMine) fieldString += 'X'
                    else if (cell.mineCount > 0) fieldString += cell.mineCount
                    else fieldString += '.'
                } else fieldString += '.'*/
                if (cell.mineCount > 0) {
                    fieldString += cell.mineCount
                } else if (cell.isMarked) fieldString += '*'
                else fieldString += '.'
            }
            fieldString += "|\n"
        }
        fieldString += "—|"
        repeat(width) {
            fieldString += '—'
        }
        fieldString += '|'
        fieldString += '\n'
        println(fieldString)
    }

    /**
     * Checks a random cell, if there is no mine adds a mine, else calls itself again
     */
    private fun addMine() {
        val x = random.nextInt(0, height)
        val y = random.nextInt(0, width)
        if (mineField[x][y].isMine) addMine()
        else {
            mineField[x][y].isMine = true
            mineField[x][y].isOpen = false
        }
    }
}

fun main() {
    println("How many mines do you want on the field?")
    val minesweeper = Minesweeper( mines = readln().toInt() )
    minesweeper.startGame()
}
