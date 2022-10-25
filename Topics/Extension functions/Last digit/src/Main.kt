import kotlin.math.abs

fun Int.lastDigit(): Int = if (this < 0) abs(this) % 10 else this % 10
