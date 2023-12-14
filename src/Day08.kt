data class Node(val nodePoint: String, val left: String, val right: String)

fun main() {

    fun createMap(input: List<String>): Pair<String, Map<String, Pair<String, String>>> {
        val instructions = input[0]
        val map =  input.asSequence().drop(2).associate {
            val (from, destinations) = it.split(" = ")
            val (left, right) = destinations.substring(1, destinations.length - 1).split(", ")
            from to (left to right)
        }
        return instructions to map
    }

    fun part1(input: List<String>): Int {
        val (instructions, map) = createMap(input)
        var i = 0
        var count = 0
        var current = "AAA"
        val target = "ZZZ"
        while (true) {
            count++
            val (l, r) = map[current]!!
            current = if(instructions[i] == 'L') l else r
            if (current == target) break
            i++
            if (i >= instructions.length)
                i=0

        }
        return count
    }

    fun lcm(a: Long, b: Long): Long {
        var ma = a
        var mb = b
        var remainder: Long

        while (mb != 0L) {
            remainder = ma % mb
            ma = mb
            mb = remainder
        }

        return a * b / ma
    }

    fun part2(input: List<String>): Long {
        val (instructions, map) = createMap(input)
        val currents = map.keys.filter { it.last() == 'A' }
        val result = mutableMapOf<String, Long>()

        currents.asSequence().forEach { current ->
            var i = 0
            var count = 0L
            var currentPos = current
            while (true) {
                count++
                val (l, r) = map[currentPos]!!
                currentPos = if(instructions[i] == 'L') l else r
                if (currentPos.endsWith("Z")) break
                i++
                if (i >= instructions.length)
                    i=0

            }
            result[current] = count
        }
        var finalResult = result.values.toList()[0]
        for (i in 1 until result.values.size) {
            finalResult = lcm(finalResult, result.values.toList()[i])
        }
        return finalResult
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}