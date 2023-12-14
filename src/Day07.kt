enum class HandType(val value: Int) {
    HighCard(6),
    OnePair(5),
    TwoPair(4),
    ThreeOfAKind(3),
    FullHouse(2),
    FourOfAKind(1),
    FiveOfAKind(0)
}

data class Hand(val hand: String, val bid: Int)
fun String.calculateType(): HandType {
    val counts = this.groupingBy { it }.eachCount().values
    return when {
        5 in counts -> HandType.FiveOfAKind
        4 in counts -> HandType.FourOfAKind
        3 in counts && 2 in counts -> HandType.FullHouse
        3 in counts -> HandType.ThreeOfAKind
        counts.count{it == 2} == 2 -> HandType.TwoPair
        2 in counts -> HandType.OnePair
        else -> HandType.HighCard
    }
}

fun String.calculateTypeWithJokers(): HandType {
    val baseType = this.filter{it != 'J'}.calculateType()
    val upgrades = this.filter { it == 'J'}.count()
    return (1..upgrades).fold(baseType) { handType: HandType, _ ->
        when(handType) {
            HandType.FiveOfAKind -> HandType.FiveOfAKind
            HandType.FourOfAKind -> HandType.FiveOfAKind
            HandType.FullHouse -> HandType.FourOfAKind
            HandType.ThreeOfAKind -> HandType.FourOfAKind
            HandType.TwoPair -> HandType.FullHouse
            HandType.OnePair -> HandType.ThreeOfAKind
            HandType.HighCard -> HandType.OnePair
            else -> error("No way")
        }
    }
}

fun main() {
    val part1Strength = "AKQJT98765432".reversed()
    val part2Strength = "AKQT98765432J".reversed()
    val alphabet = "abcdefghijklm"

    fun parse(inputString: String): Hand {
        val (hand, bid) = inputString.split(" ")
        return Hand(hand, bid.toInt())
    }

    fun String.alphabetize(strength: String): String = map { char -> alphabet[strength.indexOf(char)] }.toString()

    fun part1(input: List<String>): Int {
        return input.map(::parse)
            .sortedWith(compareBy({ it.hand.calculateType().ordinal}, { it.hand.alphabetize(part1Strength) }))
            .mapIndexed { index, hand -> hand.bid * (index + 1) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map(::parse)
            .sortedWith(compareBy({ it.hand.calculateTypeWithJokers().ordinal}, { it.hand.alphabetize(part2Strength) }))
            .mapIndexed { index, hand -> hand.bid * (index + 1) }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}