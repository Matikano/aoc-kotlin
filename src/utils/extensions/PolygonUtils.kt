package utils.extensions

import utils.models.Position
import java.math.BigInteger

fun calculateArea(
    corners: List<Position>,
    boundaryPointsNumber: Long,
    includeBoundary: Boolean = true
): BigInteger {
    // Calculating area of a polygon given its corner positions and number of all boundary points
    var area = BigInteger.ZERO
    val boundaryPointsBI = BigInteger.valueOf(boundaryPointsNumber)

    corners.forEachIndexed { index, current ->
        val previous = corners[(index - 1).mod(corners.size)]
        val next = corners[(index + 1).mod(corners.size)]

        area = area + BigInteger.valueOf(current.colIndex.toLong() * (next.rowIndex - previous.rowIndex))
    }

    area = area.abs().div(BigInteger.TWO)

    val interiorArea = (area - boundaryPointsBI.div(BigInteger.TWO))

    return if (includeBoundary) interiorArea + boundaryPointsBI else interiorArea
}