enum class SpaceType {
    GALAXY, EMPTY;

    companion object {
        fun from(char: Char): SpaceType = when (char) {
            '#' -> GALAXY
            '.' -> EMPTY
            else -> throw UnsupportedOperationException()
        }
    }
}

data class SpacePos(val x: Int, val y: Int, val type: SpaceType)

class SpaceImage(val map: List<List<SpacePos>>) {

    private val galaxies: List<SpacePos> by lazy { map.flatten().filter { it.type == SpaceType.GALAXY } }

    private fun isExpandingColumn(x: Int) = map.all { it[x].type == SpaceType.EMPTY }
    private fun isExpandingRow(y: Int) = map[y].all { it.type == SpaceType.EMPTY }

    fun shortestPathSum(coefficient: Long): Long {
        val galaxyPairs = galaxies.flatMapIndexed { i, pos1 ->
            galaxies.subList(i + 1, galaxies.size).map { pos2 -> pos1 to pos2 }
        }
        return galaxyPairs.sumOf { (pos1, pos2) ->
            val (xRange, yRange) = getRanges(pos1.x, pos2.x, pos1.y, pos2.y)
            val sumX = calculateSum(xRange, this::isExpandingColumn, coefficient)
            val sumY = calculateSum(yRange, this::isExpandingRow, coefficient)
            sumX + sumY
        }
    }

    private fun getRanges(x1: Int, x2: Int, y1: Int, y2: Int): Pair<IntRange, IntRange> {
        val xRange = if (x1 <= x2) x1 until x2 else x2 until x1
        val yRange = if (y1 <= y2) y1 until y2 else y2 until y1
        return xRange to yRange
    }

    private fun calculateSum(range: IntRange, isExpanding: (Int) -> Boolean, coefficient: Long): Long {
        return range.sumOf { if (isExpanding(it)) coefficient.toInt() else 1 }.toLong()
    }


    companion object {
        fun transform(input: List<String>): SpaceImage {
            return SpaceImage(input.mapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    SpacePos(
                        x,
                        y,
                        SpaceType.from(char)
                    )
                }
            })
        }
    }
}

fun main() {

    fun part1(input: List<String>): Long = SpaceImage.transform(input).shortestPathSum(2)

    fun part2(input: List<String>): Long = SpaceImage.transform(input).shortestPathSum(1000000)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    part1(testInput).println()
    check(part1(testInput) == 374L)
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}