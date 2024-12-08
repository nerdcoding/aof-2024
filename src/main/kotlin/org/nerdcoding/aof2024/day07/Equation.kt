package org.nerdcoding.aof2024.day07

class Equation(val result: Long, private val operands: List<Long>) {

    fun isSolvable(possibleOperations: Set<Operation>): Boolean {
        return recursiveCalculation(operands.first(), 0, possibleOperations)
    }

    private fun recursiveCalculation(
            leftOperand: Long,
            operandsIndex: Int,
            possibleOperations: Set<Operation>): Boolean =
        when {
            leftOperand > result -> false
            operandsIndex + 1 >= operands.size -> leftOperand == result
            else -> possibleOperations.any {
                operation -> recursiveCalculation(
                    operation.calculate(leftOperand, operands[operandsIndex + 1]),
                operandsIndex + 1,
                    possibleOperations
                )
            }
        }

}