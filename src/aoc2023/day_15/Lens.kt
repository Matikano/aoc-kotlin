package aoc2023.day_15

data class Lens(
    val label: String,
    val focalLength: Int
){
    override fun equals(other: Any?): Boolean {
        if (other is Lens)
            return label == other.label
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return label.hashCode()
    }

    override fun toString(): String = "[$label $focalLength]"
}
