fun main() {
    val pairings = listOf( "one" to "on1e", "two" to "tw2o", "three" to "thr3ee", "four" to "fo4ur", "five" to "fi5ve", "six" to "si6x", "seven" to "sev7en","eight" to "eig8ht", "nine" to "ni9ne")
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
                .smartReplace(pairings)
                .filter { it.isDigit() }
                .getFirstAndLast()
                .toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
