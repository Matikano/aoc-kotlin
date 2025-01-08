package utils.models

sealed interface Operation {
    val symbol: String
    fun compute(a: Long, b: Long): Long

    data object Add: Operation {
        override fun compute(a: Long, b: Long): Long = a + b
        override val symbol: String
            get() = "+"
    }

    data object Multiply: Operation {
        override fun compute(a: Long, b: Long): Long = a * b
        override val symbol: String
            get() = "*"
    }

    data object Subtract: Operation {
        override fun compute(a: Long, b: Long): Long = a - b
        override val symbol: String
            get() = "-"
    }

    data object Divide: Operation {
        override fun compute(a: Long, b: Long): Long = a / b
        override val symbol: String
            get() = "/"
    }

    companion object {
        fun String.toOperation(): Operation =
            when(this) {
                "+" -> Add
                "*" -> Multiply
                "-" -> Subtract
                "/" -> Divide
                else -> throw IllegalArgumentException("Unsupported String = $this for Operation mapping")
            }
    }
}