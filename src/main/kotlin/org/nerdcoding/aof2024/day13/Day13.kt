package org.nerdcoding.aof2024.day13



fun main() {
    val resultPart1 = InputPuzzleReader().readInputFile(InputPuzzleReader.Part.PART1)
        .map { clawMachine -> evaluateClawMachine2(clawMachine) }
        .filterNotNull()
        .map { buttonPush -> calculatePrice(buttonPush) }
        .sumOf { it }
    println("Result part1: $resultPart1")

    val resultPart2 = InputPuzzleReader().readInputFile(InputPuzzleReader.Part.PART2)
        .map { clawMachine -> evaluateClawMachine2(clawMachine) }
        .filterNotNull()
        .map { buttonPush -> calculatePrice(buttonPush) }
        .sumOf { it }
    println("Result part2: $resultPart2")
}

private fun evaluateClawMachine2(clawMachine: ClawMachine): ButtonPush? {

    var a = ((clawMachine.prize.xLocation * clawMachine.buttonB.yMovement) - (clawMachine.prize.yLocation * clawMachine.buttonB.xMovement)).toDouble() / ((clawMachine.buttonA.xMovement * clawMachine.buttonB.yMovement) - (clawMachine.buttonA.yMovement * clawMachine.buttonB.xMovement)).toDouble()
    var b = ((clawMachine.prize.xLocation * clawMachine.buttonA.yMovement) - (clawMachine.prize.yLocation * clawMachine.buttonA.xMovement)).toDouble() / ((clawMachine.buttonA.yMovement * clawMachine.buttonB.xMovement) - (clawMachine.buttonA.xMovement * clawMachine.buttonB.yMovement)).toDouble()
    if (a % 1 == 0.0 && b % 1 == 0.0 ) {
        return ButtonPush(a.toLong(), b.toLong())
    }
    return null

    /*for (x2 in 1..maxSearch) {
        val x1Numerator = clawMachine.prize.xLocation - clawMachine.buttonB.xMovement * x2
        if (x1Numerator % clawMachine.buttonA.xMovement == 0L) {
            val x1 = x1Numerator / clawMachine.buttonA.xMovement
            if (clawMachine.buttonA.yMovement * x1 + clawMachine.buttonB.yMovement * x2 == clawMachine.prize.yLocation) {
                return ButtonPush(x1, x2)
            }
        }
    }
    return null*/
}

private fun calculatePrice(buttonPush: ButtonPush): Long =
    buttonPush.numberOfPushButtonA*3 + buttonPush.numberOfPushButtonB

class ClawMachine(val buttonA: Button, val buttonB: Button, val prize: Prize) {
    override fun toString(): String {
        return "ClawMachine(buttonA=$buttonA, buttonB=$buttonB, prize=$prize)"
    }
}
class Button(val xMovement: Long, val yMovement: Long) {
    override fun toString(): String {
        return "Button(xMovement=$xMovement, yMovement=$yMovement)"
    }
}
class Prize(val xLocation: Long, val yLocation: Long) {
    override fun toString(): String {
        return "Prize(xLocation=$xLocation, yLocation=$yLocation)"
    }
}
class ButtonPush(val numberOfPushButtonA: Long, val numberOfPushButtonB: Long) {
    override fun toString(): String {
        return "ButtonPush(numberOfPushButtonA=$numberOfPushButtonA, numberOfPushButtonB=$numberOfPushButtonB)"
    }
}