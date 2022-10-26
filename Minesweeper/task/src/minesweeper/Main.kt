package minesweeper

import java.util.Random
import java.util.Scanner

val scanner: Scanner = Scanner(System.`in`)
val random: Random = Random()

/**
 * Represents a single cell in the minefield and stores its states
 * @param isMine if there is a mine in the cell or not
 * @param isOpen if the cell is opened by the player or not
 * @param isMarked if the cell is marked by the player as a mine or not
 * @param isChecked if the cell is checked before or not
 * @param mineCount count of mines around
 */
data class Cell(
    var isMine: Boolean = false,
    var isOpen: Boolean = false,
    var isMarked: Boolean = false,
    var isChecked: Boolean = false,
    var mineCount: Int = 0
)

/**
 * Initializes the game by the given parameters and controls the flow of the game
 * @param mines count of mines in the game
 * @param width width of the minefield
 * @param height height of the minefield
 * @property minefield matrix that represents the minefield
 */
class Minesweeper(mines: Int, width: Int = 9, height: Int = 9) {
    private val mines: Int
    private val width: Int
    private val height: Int
    private val minefield: MutableList<MutableList<Cell>>

    init {
        this.mines = mines
        this.width = width
        this.height = height
        this.minefield = MutableList(width) {
            MutableList(height) { Cell() } // an empty minefield with the given sizes
        }
        printField()
    }

    /**
     * Starts the game and initializes the minefield with given number of mines
     */
    fun startGame() {
        println("Set/unset mines marks or claim a cell as free:")
        val x = scanner.nextInt() - 1
        val y = scanner.nextInt() - 1
        repeat(mines) { addMine(x, y) }
        countMines()
        lookAround(x, y)
        printField()
    }

    /**
     * Prints the field to the console each time and controls the flow of the game with two conditions:
     * - Did player stepped a mine
     * - Did user won
     */
    fun playGame() {
        while (makeOperation()) {
            printField()
            if (playerWins()) break
        }
    }

    /**
     * Checks if player wins the game in current state
     * @return true if player wins, false otherwise
     */
    private fun playerWins(): Boolean {
        repeat(width) { x ->
            repeat(height) { y ->
                val cell = minefield[x][y]
                if (cell.isMine != cell.isMarked) return false
            }
        }
        println("Congratulations! You found all the mines!")
        return true
    }

    /**
     * Runs the commands entered by the player and checks if player can continue to the game
     * @return false if player looses, true otherwise
     */
    private fun makeOperation(): Boolean {
        println("Set/unset mines marks or claim a cell as free:")
        val x = scanner.nextInt() - 1
        val y = scanner.nextInt() - 1
        when (scanner.next()) { // gets the type of the operation
            "mine" -> minefield[x][y].isMarked = !minefield[x][y].isMarked
            "free" -> if (minefield[x][y].isMine) {
                gameOver()
                return false
            } else
                lookAround(x, y)
        }
        return true
    }

    /**
     * Opens all cells with mines and gives game over message to the player
     */
    private fun gameOver() {
        repeat(width) { x ->
            repeat(height) { y ->
                val cell = minefield[x][y]
                if (cell.isMine) cell.isOpen = true
            }
        }
        printField()
        println("You stepped on a mine and failed!")
    }

    /**
     * Marks the cell as checked and opens it, if this cell is not a number and the cells around are empty calls itself for those cells too
     */
    private fun lookAround(x: Int, y: Int) {
        val cell = minefield[x][y]
        cell.isOpen = true
        cell.isMarked = false
        cell.isChecked = true
        if (cell.mineCount == 0) {
            for (i in x - 1..x + 1)
                for (j in y - 1..y + 1)
                    if (
                        i in 0 until width
                        && j in 0 until height
                        && (i != x || j != y)
                        && !minefield[i][j].isChecked
                    )
                        lookAround(i, j) // look around cells around
        }
    }

    /**
     * Checks cells in the neighborhood and increases its [Cell.mineCount] property by 1 each time finds a mine
     */
    private fun countMines() {
        repeat(width) { x ->
            repeat(height) { y ->
                val cell = minefield[x][y]
                if (!cell.isMine)
                    for (i in x - 1..x + 1)
                        for (j in y - 1..y + 1)
                            if (i in 0 until width // index not out of range
                                && j in 0 until height // index not out of range
                                && (i != x || j != y) // the looking cell isn't itself
                                && minefield[i][j].isMine
                            ) // the looking cell is a mine
                                cell.mineCount++ // increase count of mines around
            }
        }
    }

    /**
     * Adds a mine to a random empty cell except the starting cell
     */
    private fun addMine(startX: Int, startY: Int) {
        val x = random.nextInt(0, width)
        val y = random.nextInt(0, height)
        val cell = minefield[x][y]
        if (cell.isMine || startX == x || startY == y) addMine(startX, startY)
        else cell.isMine = true
    }

    /**
     * Prints the minefield to the console:
     * 1. If the cell is closed prints '.' instead of it
     * 2. If the cell is empty prints '/' instead of it
     * 3. If the cell represents number of mines around it prints that number
     * 4. If there is a mine in the cell prints 'X'
     * 5. If the cell is marked prints '*'
     */
    private fun printField() {
        var fieldString = " |"
        repeat(width) { fieldString += it + 1 }; fieldString += '|'
        fieldString += "\n—|"; repeat(width) { fieldString += '—' }; fieldString += '|'
        repeat(height) { y ->
            fieldString += "\n${y + 1}|"
            repeat(width) { x ->
                val cell = minefield[x][y]
                fieldString += if (cell.isMarked) '*'
                else if (cell.isOpen) {
                    if (cell.isMine) 'X'
                    else if (cell.mineCount == 0) '/'
                    else cell.mineCount
                } else '.'
            }
            fieldString += '|'
        }
        fieldString += "\n—|"; repeat(width) { fieldString += '—' }; fieldString += '|'
        println(fieldString)
    }
}

fun main() {
    println("How many mines do you want on the field?")
    val minesweeper = Minesweeper(mines = scanner.nextInt())
    minesweeper.startGame()
    minesweeper.playGame()
}
