import kotlin.math.absoluteValue

fun List<List<String>>.parseMap(mapName: String) : List<Pair<LongRange, LongRange>> {
    val filteredRanges = this.filter { it[0].contains(mapName) }.first().drop(1)
    val mappedRanges = filteredRanges.flatMap { str ->
        val regex = Regex("\\d+")
        regex.findAll(str).map { it.value.toLong() }
    }.toList()
    return mappedRanges.chunked(3) { (start, end, range) ->
        (end until end + range) to (start until start + range)
    }
}

fun List<Pair<LongRange, LongRange>>.getNextNumber(source: Long) : Long {
    val ranges = this.firstOrNull{it.first.contains(source)} ?: return source
    return ranges.second.first + (source - ranges.first.first).absoluteValue
}

fun List<Pair<LongRange, LongRange>>.getPreviousNumber(source: Long) : Long {
    val ranges = this.firstOrNull{it.second.contains(source)} ?: return source
    return ranges.first.first + (source - ranges.second.first).absoluteValue
}
fun main() {

    fun parse(input: List<String>) : List<List<String>> {
        val result = mutableListOf<List<String>>()
        val currentGroup = mutableListOf<String>()

        for (item in input) {
            if (item.isNotEmpty()) {
                currentGroup.add(item)
            } else if (currentGroup.isNotEmpty()) {
                result.add(currentGroup.toList())
                currentGroup.clear()
            }
        }

        if (currentGroup.isNotEmpty()) {
            result.add(currentGroup.toList())
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val parsedList = parse(input)
        val regex = Regex("\\d+")
        val seeds = regex.findAll(parsedList[0][0]).map { it.value.toLong() }.toList() // we have our seeds.

        val seedToSoilRanges = parsedList.parseMap("seed-to-soil")
        val soilToFertilizerRanges = parsedList.parseMap("soil-to-fertilizer")
        val fertilizerToWaterRanges = parsedList.parseMap("fertilizer-to-water")
        val waterToLightRanges = parsedList.parseMap("water-to-light")
        val lightToTemperatureRanges = parsedList.parseMap("light-to-temperature")
        val temperatureToHumidityRanges = parsedList.parseMap("temperature-to-humidity")
        val humidityToLocationRanges = parsedList.parseMap("humidity-to-location")

        val seedToLocation = mutableMapOf<Long, Long>()

        seeds.forEach { seed ->
            val soil = seedToSoilRanges.getNextNumber(seed)
            val fertilizer = soilToFertilizerRanges.getNextNumber(soil)
            val water = fertilizerToWaterRanges.getNextNumber(fertilizer)
            val light = waterToLightRanges.getNextNumber(water)
            val temperature = lightToTemperatureRanges.getNextNumber(light)
            val humidity = temperatureToHumidityRanges.getNextNumber(temperature)
            val location = humidityToLocationRanges.getNextNumber(humidity)
            seedToLocation[seed] = location
        }
        return seedToLocation.minBy { it.value }.value
    }


    fun part2(input: List<String>): Long {
        val parsedList = parse(input)
        val regex = Regex("\\d+")
        val seeds = regex.findAll(parsedList[0][0]).map { it.value.toLong() }.windowed(2, 2).map { it[0] until it[0] + it[1] }.toList()

        val seedToSoilRanges = parsedList.parseMap("seed-to-soil")
        val soilToFertilizerRanges = parsedList.parseMap("soil-to-fertilizer")
        val fertilizerToWaterRanges = parsedList.parseMap("fertilizer-to-water")
        val waterToLightRanges = parsedList.parseMap("water-to-light")
        val lightToTemperatureRanges = parsedList.parseMap("light-to-temperature")
        val temperatureToHumidityRanges = parsedList.parseMap("temperature-to-humidity")
        val humidityToLocationRanges = parsedList.parseMap("humidity-to-location")

       var minValue = Long.MAX_VALUE

        for (loc in 1L until 100_000_000L) {
            val humidity = humidityToLocationRanges.getPreviousNumber(loc)
            val temperature = temperatureToHumidityRanges.getPreviousNumber(humidity)
            val light = lightToTemperatureRanges.getPreviousNumber(temperature)
            val water = waterToLightRanges.getPreviousNumber(light)
            val fertilizer = fertilizerToWaterRanges.getPreviousNumber(water)
            val soil = soilToFertilizerRanges.getPreviousNumber(fertilizer)
            val seed = seedToSoilRanges.getPreviousNumber(soil)

            if (seeds.any { it.contains(seed) }) {
                minValue = loc
                break
            }
        }
        return minValue
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}


