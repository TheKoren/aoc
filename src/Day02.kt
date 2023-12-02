data class Game(
    var bags: List<Bag>,
    val id: Int
)

data class Bag(
    val red: Int,
    val green: Int,
    val blue: Int
)

const val MAX_RED_CUBES = 12
const val MAX_GREEN_CUBES = 13
const val MAX_BLUE_CUBES = 14

fun main() {

    fun parse(gameLine: String): Game {
        val (gameIdString, bagsString) = gameLine.split(":")
        val gameId = gameIdString.filter { it.isDigit() }.toInt()

        val bags = bagsString.split(";").map { bag ->
            var redCube = 0
            var greenCube = 0
            var blueCube = 0

            bag.split(",").forEach { cube ->
                when {
                    cube.contains("red") -> redCube = cube.filter { it.isDigit() }.toInt()
                    cube.contains("green") -> greenCube = cube.filter { it.isDigit() }.toInt()
                    cube.contains("blue") -> blueCube = cube.filter { it.isDigit() }.toInt()
                }
            }

            Bag(redCube, greenCube, blueCube)
        }

        return Game(bags, gameId)
    }


    fun part1(input: List<String>): Int {
        return input
            .map(::parse)
            .filter { game ->
                game.bags.all {
                    it.red <= MAX_RED_CUBES && it.blue <= MAX_BLUE_CUBES && it.green <= MAX_GREEN_CUBES
                }
            }
            .sumOf { it.id }
    }

    fun minimumPowerCalculate(game: Game): Int {
        val maxRed = game.bags.map { it.red }.max()
        val maxBlue = game.bags.map { it.blue }.max()
        val maxGreen = game.bags.map { it.green }.max()
        return maxRed * maxBlue * maxGreen
    }

    fun part2(input: List<String>): Int {
        return input
            .map(::parse)
            .map(::minimumPowerCalculate)
            .sum()
    }

    //test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
