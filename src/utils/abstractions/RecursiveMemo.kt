package utils.abstractions

abstract class RecursiveMemo1<INPUT, RESULT> : (INPUT) -> RESULT {
    private val memo = mutableMapOf<INPUT, RESULT>()
    override fun invoke(input: INPUT): RESULT = memo.recurse(input)
    abstract fun MutableMap<INPUT, RESULT>.recurse(input: INPUT): RESULT
}

abstract class RecursiveMemo2<INPUT1, INPUT2, RESULT> : (INPUT1, INPUT2) -> RESULT {
    private val memo = mutableMapOf<Pair<INPUT1, INPUT2>, RESULT>()
    override fun invoke(input1: INPUT1, input2: INPUT2): RESULT = memo.recurse(input1, input2)
    abstract fun MutableMap<Pair<INPUT1, INPUT2>, RESULT>.recurse(input1: INPUT1, input2: INPUT2): RESULT
}