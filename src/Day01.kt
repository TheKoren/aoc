fun main() {
    val fixNumbers = listOf("oneight" to "oneeight", "threeight" to "threeeight", "fiveight" to "fiveeight", "nineight" to "nineeight", "twone" to "twoone", "sevenine" to "sevennine", "eightwo" to "eighttwo")
    val pairings = listOf( "one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7","eight" to "8", "nine" to "9")
    fun String.getFirstAndLast() = "${first()}${last()}"
    fun String.smartReplace(replacements: List<Pair<String, String>>) : String {
        var result = this
        replacements.forEach { (l, r) -> result = result.replace(l, r) }
        return result
    }
    fun part1(input: List<String>): Int {

        return input.sumOf { calibration ->
            calibration
                .filter { it.isDigit() }
                .getFirstAndLast()
                .toInt()

        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { calibration ->
            calibration
                .smartReplace(fixNumbers)
                .smartReplace(pairings)
                .filter { it.isDigit() }
                .getFirstAndLast()
                .toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 142)
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
