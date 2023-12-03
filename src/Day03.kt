import kotlin.math.absoluteValue

open class Position (val row: Int, val range: IntRange)
class Number (val value: Int, row: Int, range: IntRange) : Position(row, range)
class Symbol (val value: Char, row: Int, range: IntRange) : Position(row, range)

fun main() {

    fun parse(line: String, row: Int) : Sequence<Position>{
        // mutableLine to handle duplicates
        var mutableLine = line
        mutableLine = StringBuilder(mutableLine).insert(mutableLine.length, ".").insert(0, ".").toString()
        val regex = Regex("\\d+")
        val numbersWithPosition = regex.findAll(mutableLine).map { numberMatch ->
            val number = numberMatch.value
            val startIdx = mutableLine.indexOf(number)
            mutableLine = mutableLine.replaceFirst(number, ".".repeat(number.length))
            Number(number.toInt(), row, startIdx..(startIdx + number.length))
        }.toList()
        val symbolsWithPosition = mutableLine.filter { it != '.' && !it.isDigit()}.map { symbol ->
            val startIdx = mutableLine.indexOf(symbol)
            mutableLine = mutableLine.replaceFirst(symbol, '.')
            Symbol(symbol, row, startIdx..startIdx + 1)
        }.toList()
        return (numbersWithPosition+symbolsWithPosition).asSequence()
    }

    fun parse(input: List<String>): Sequence<Position> {
        return input.mapIndexed { index, line ->  parse(line, index)}.asSequence().flatMap { it }
    }


    fun part1(input: List<String>): Int {
        val elements = parse(input).toList()
        val numbers = elements.filterIsInstance<Number>()
        val symbols = elements.filterIsInstance<Symbol>().groupBy { it.row }
        return numbers.filter {
            n -> symbols.getNeighbouring(n.row).any{c -> n.overlap(c)}
        }.sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val elements = parse(input).toList()
        val numbers = elements.filterIsInstance<Number>().groupBy { it.row }
        val gears = elements.filterIsInstance<Symbol>().filter { it.value == '*' }
        return gears.map{g->
            numbers.getNeighbouring(g.row).filter { it.overlap(g) }.toList()
        }.filter { it.size == 2 }.sumOf { it[0].value * it[1].value }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    part1(testInput).println()
    check(part1(testInput) == 4361)
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun Number.overlap(c: Symbol): Boolean {
    if ((this.row - c.row).absoluteValue <= 1) {
        return this.range.overlap(c.range)
    }
    return false
}

private fun IntRange.overlap(range: IntRange): Boolean {
    var a = this
    var b = range

    if (a.first > b.first) {
        a = range; b = this
    }
    return b.first <= a.last
}

private fun <T> Map<Int, List<T>>.getNeighbouring(rowId: Int): List<T> {
    return (rowId -1 .. rowId + 1).map { this.getOrDefault(it, listOf()) }.flatMap { it.asSequence() }
}
