package aoc2023.day_20

data class Pulse(
    val source: String,
    val destination: String,
    val strength: Strength
) {
    override fun toString(): String = "$source -${strength.name.lowercase()}-> $destination"

    enum class Strength {
        LOW,
        HIGH
    }
}
