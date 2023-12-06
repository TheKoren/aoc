import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    fun solveQuadraticEquation(t: Long, d: Long): Pair<Long, Long> {
        val discriminant = t * t - 4 * (-1) * -d
        val root1 = (-t + sqrt(discriminant.toDouble())) / (2 * -1)
        val root2 = (-t - sqrt(discriminant.toDouble())) / (2 * -1)
        return Pair(floor(root1).toLong(), ceil(root2).toLong())
    }

    fun extractLongValues(input1: String, input2: String): Pair<List<Long>, List<Long>> =
        Pair(
            Regex("\\d+").findAll(input1).map { it.value.toLong() }.toList(),
            Regex("\\d+").findAll(input2).map { it.value.toLong() }.toList()
        )

    fun part1(input: List<String>): Long {
        val (timeList, distanceList) = extractLongValues(input[0], input[1])
        val mapForQuadratic: Map<Long, Long> = timeList.zip(distanceList).toMap()
        val pairList = mapForQuadratic.map { solveQuadraticEquation(it.key, it.value) }

        return pairList.map { it.second - it.first - 1 }.fold(1L) { acc, e -> acc * e }
    }

    fun part2(input: List<String>): Long {
        val modifiedInput = input.map { str ->
            str.filter { it.isDigit() }
        }
        val (timeList, distanceList) = extractLongValues(modifiedInput[0], modifiedInput[1])
        val mapForQuadratic: Map<Long, Long> = timeList.zip(distanceList).toMap()
        val pairList = mapForQuadratic.map { solveQuadraticEquation(it.key, it.value) }
        return pairList.map { it.second - it.first - 1 }.fold(1L) { acc, e -> acc * e }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
