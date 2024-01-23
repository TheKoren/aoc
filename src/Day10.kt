data class PipePos(val x: Int, val y: Int) {
    // use get() for lazy init
    val east get() = go(Direction.EAST)
    val west get() = go(Direction.WEST)
    val north get() = go(Direction.NORTH)
    val south get() = go(Direction.SOUTH)

    fun go(direction: Direction): PipePos {
        return when (direction) {
            Direction.WEST -> copy(x = x - 1)
            Direction.NORTH -> copy(y = y - 1)
            Direction.SOUTH -> copy(y = y + 1)
            Direction.EAST -> copy(x = x + 1)
        }
    }
}

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    fun backwards() = entries[(ordinal + 2).rem(entries.size)]

}

enum class Pipe(val char: Char, val connections: Set<Direction>) { //Name: Open from which sides
    NORTH_AND_SOUTH('|', setOf(Direction.NORTH, Direction.SOUTH)),
    EAST_AND_WEST('-', setOf(Direction.EAST, Direction.WEST)),
    NORTH_AND_EAST('L', setOf(Direction.NORTH, Direction.EAST)),
    NORTH_AND_WEST('J', setOf(Direction.NORTH, Direction.WEST)),
    SOUTH_AND_WEST('7', setOf(Direction.SOUTH, Direction.WEST)),
    SOUTH_AND_EAST('F', setOf(Direction.SOUTH, Direction.EAST)),
    GROUND('.', emptySet()),
    START('S', emptySet());

    fun nextDirection(lastDirection: Direction) = connections.first { it != lastDirection.backwards() }

    companion object {
        fun transform(char: Char): Pipe = entries.first { it.char == char }

        fun getByDirections(direction1: Direction, direction2: Direction): Pipe? {
            val directionsSet = setOf(direction1, direction2)

            return entries.find { it.connections == directionsSet }
        }
    }
}

class Maze(val map: List<List<Pipe>>) {
    private val start: PipePos = run {
        val y = map.indexOfFirst { it.contains(Pipe.START) }
        val x = map[y].indexOfFirst { it == Pipe.START }
        PipePos(x, y)
    }

    private fun at(pos: PipePos) = map[pos.y][pos.x]

    private val firstStepDirection = when {
        at(start.east).connections.contains(Direction.WEST) -> Direction.EAST
        at(start.south).connections.contains(Direction.NORTH) -> Direction.SOUTH
        at(start.west).connections.contains(Direction.EAST) -> Direction.WEST
        at(start.north).connections.contains(Direction.SOUTH) -> Direction.NORTH
        else -> throw IllegalStateException()
    }

    fun traverseLoop(): Sequence<Pair<PipePos, Pipe>> = sequence {
        var position = start
        var direction = firstStepDirection
        var pipe: Pipe
        do {
            position = position.go(direction)
            pipe = at(position)
            yield(Pair(position, pipe))
            if (pipe != Pipe.START) direction = pipe.nextDirection(direction)
        } while (pipe != Pipe.START)
    }

    fun replaceStart(loop: Set<Pair<PipePos, Pipe>>) : Pair<PipePos, Pipe> {
        val lastElements = loop.toList().takeLast(2)
        val lastDirection = when {
            at(lastElements[0].first.east).connections.isEmpty() -> Direction.EAST
            at(lastElements[0].first.north).connections.isEmpty() -> Direction.NORTH
            at(lastElements[0].first.south).connections.isEmpty() -> Direction.SOUTH
            at(lastElements[0].first.west).connections.isEmpty() -> Direction.WEST
            else -> throw IllegalStateException()
        }

        return Pair(lastElements[1].first, Pipe.getByDirections(firstStepDirection, lastDirection.backwards())!!)
    }

    companion object {
        fun transform(input: List<String>): Maze = Maze(input.map { line -> line.map { char -> Pipe.transform(char) } })
    }
}

fun main() {



    fun part1(input: List<String>): Int {
        return Maze.transform(input).traverseLoop().count() / 2
    }

    fun part2(input: List<String>): Int {
        val maze = Maze.transform(input)
        val loop = maze.traverseLoop().toSet() as HashSet
        val replacedStart = maze.replaceStart(loop)
        loop.remove(Pair(replacedStart.first, Pipe.START))
        loop.add(replacedStart)

        var count = 0
        maze.map.mapIndexed { y, pipes ->
            var inRegion = false
            var cornerF = false
            var cornerL = false
            pipes.mapIndexed { x, pipe ->
                val tile = if (pipe == Pipe.START) replacedStart.second else pipe
                if (loop.contains(Pair(PipePos(x, y), tile))) {
                    when (tile.char) {
                        'F' -> {
                            cornerF = true
                            cornerL = false
                        }
                        'L' -> {
                            cornerL = true
                            cornerF = false
                        }
                    }

                    val corner = (tile.char == '7' && cornerF) || (tile.char == 'J' && cornerL)

                    if (tile.char in setOf('F', 'L', '-'))
                    {
                        //Do nothing
                    } else if (!corner) {
                        inRegion = !inRegion
                        cornerF = false
                        cornerL = false
                    }
                } else if (inRegion) {
                    count++
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part2(testInput) == 10)
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}