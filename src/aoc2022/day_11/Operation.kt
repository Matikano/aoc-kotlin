package aoc2022.day_11

sealed interface Operation {
    fun compute(a: Long, b: Long): Long

    data object Add: Operation {
        override fun compute(a: Long, b: Long): Long = a + b
    }

    data object Multiply: Operation {
        override fun compute(a: Long, b: Long): Long = a * b
    }

    companion object {
        fun String.toOperation(): Operation =
            when(this) {
                "+" -> Add
                "*" -> Multiply
                else -> throw IllegalArgumentException("Unsupported String = $this for Operation mapping")
            }
    }
}