package utils.abstractions

abstract class RecursiveMemo1<INPUT, RESULT> : (INPUT) -> RESULT {
    private val memo = mutableMapOf<INPUT, RESULT>()
    override fun invoke(input: INPUT): RESULT = compute(input)
    private fun compute(input: INPUT): RESULT = memo.getOrPut(input) {
        recurse.invoke(input)
    }

    abstract val recurse: (INPUT) -> RESULT
}

abstract class RecursiveMemo2<INPUT1, INPUT2, RESULT> : (INPUT1, INPUT2) -> RESULT {
    private val memo = mutableMapOf<Pair<INPUT1, INPUT2>, RESULT>()
    override fun invoke(input1: INPUT1, input2: INPUT2): RESULT = compute(input1, input2)
    private fun compute(input1: INPUT1, input2: INPUT2): RESULT = memo.getOrPut(input1 to input2) {
        recurse.invoke(input1, input2)
    }

    abstract val recurse: (INPUT1, INPUT2) -> RESULT
}