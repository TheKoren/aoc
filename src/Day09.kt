fun main() {

    fun List<Int>.calculateDiffs(): Sequence<List<Int>> {
        return generateSequence(this) {line : List<Int> ->
            line.zipWithNext().map { (a, b) -> b - a }
        }.takeWhile{v -> v.any{it != 0}}
    }
    fun List<Int>.extrapolateForward() : Int = this.calculateDiffs().sumOf { it.last() }

    fun List<Int>.extrapolateBackward() : Int = this.calculateDiffs().map { it.first() }.toList().reversed().fold(0) {history, diff -> diff - history}

    fun createArray(input: String): List<Int> = input.split(" ").map { it.toInt() }.toList()

    fun part1(input: List<String>): Int {
        return input.map(::createArray).sumOf { it.extrapolateForward() }
    }

    fun part2(input: List<String>): Int {
        return input.map(::createArray).sumOf { it.extrapolateBackward() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}