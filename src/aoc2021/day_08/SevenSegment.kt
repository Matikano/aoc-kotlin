package aoc2021.day_08

data class SevenSegment(
    private val input: List<String>,
    private val output: List<String>
) {
    private val charMap: MutableMap<Char, Char> = mutableMapOf()

    init {
        initializeMap()
    }

    val countOfUniqueSizeDigits: Int
        get() = output.count { it.length in listOf(2, 3, 4, 7) }

    val decipheredOutput: Int
        get() = output.map { digitMap[it.map { charMap[it] }.toSet()] }.joinToString("").toInt()

    private fun initializeMap() {
        val one = input.first { it.length == 2 }.toSet()
        val seven = input.first { it.length == 3 }.toSet()
        val four = input.first { it.length == 4 }.toSet()
        val eight = input.first { it.length == 7 }.toSet()
        val three = input.first { it.length == 5 && it.toSet().containsAll(one) }.toSet()

        val aKey = (seven - one).first()
        val bKey = (four - three).first()
        val dKey = ((four - one ) - bKey).first()

        val zero = eight - dKey
        val nine = input.first { it.length == 6 && it.toSet().containsAll(one + dKey) }.toSet()

        val eKey = (eight - nine).first()
        val six = input.first { it.toSet().containsAll(listOf(eKey, dKey)) && it.length == 6 }.toSet()

        val cKey = (eight - six).first()
        val two = input.first {
            it.length == 5 && setOf(aKey, cKey, dKey, eKey).all { key -> key in it.toSet() }
        }.toSet()

        val gKey = (two - setOf(aKey, cKey, dKey, eKey)).first()
        val fKey = (zero - two - setOf(bKey)).first()

        charMap[aKey] = 'a'
        charMap[bKey] = 'b'
        charMap[cKey] = 'c'
        charMap[dKey] = 'd'
        charMap[eKey] = 'e'
        charMap[fKey] = 'f'
        charMap[gKey] = 'g'
    }

    companion object {
        val digitMap = mapOf(
            "abcefg".toSet() to 0,
            "cf".toSet() to 1,
            "acdeg".toSet() to 2,
            "acdfg".toSet() to 3,
            "bcdf".toSet() to 4,
            "abdfg".toSet() to 5,
            "abdefg".toSet() to 6,
            "acf".toSet() to 7,
            "abcdefg".toSet() to 8,
            "abcdfg".toSet() to 9
        )
    }
}