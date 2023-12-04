import kotlin.math.pow

data class Card(val id: Int, val numberOfMatches: Int, val copiesOf: IntRange) {
    fun getMatchValue(): Int = if (numberOfMatches == 0) 0 else 2.0.pow(numberOfMatches - 1).toInt()
}
fun main() {

    fun parse(cardLine: String): Card {
        val (cardId, onlyNumbersString) = cardLine.split(":")
        val (winningNumbers, myNumbers) = onlyNumbersString.split("|")
        val regex = Regex("\\d+")

        fun extractNumbers(input: String): Set<Int> = regex.findAll(input).map { it.value.toInt() }.toSet()

        val id = extractNumbers(cardId).first()
        val interSectedList = extractNumbers(winningNumbers) intersect extractNumbers(myNumbers)
        return Card(id, interSectedList.size, id + 1 .. id + interSectedList.size)
    }

    tailrec fun List<Card>.calculate(cardCounts: Map<Int, Long> = associate { it.id to 1L }) : Map<Int, Long> {
        if (this.isEmpty()) {
            return cardCounts
        }

        val current = this.first()
        val remaining = this.drop(1)

        val currentValue = cardCounts.getValue(current.id)
        return remaining.calculate(cardCounts + current.copiesOf.associateWith { cardCounts.getValue(it) + currentValue})
    }


    fun part1(input: List<String>): Int {
        return input.map(::parse).sumOf { it.getMatchValue() }
    }

    fun part2(input: List<String>): Long {
        return input.map(::parse).calculate().values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30L)
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}


