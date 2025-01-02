package aoc2022.day_11

sealed interface Operation {
    fun compute(a: Int, b: Int): Int

    object Add : Operation {
        override fun compute(a: Int, b: Int): Int = a + b
    }

    object Multiply: Operation {
        override fun compute(a: Int, b: Int): Int = a * b
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