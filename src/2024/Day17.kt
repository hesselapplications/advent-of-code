import kotlin.math.pow

fun main() {
    val input = readInput("2024/Day17")
    
    data class Program(
        var registerA: Long,
        var registerB: Long,
        var registerC: Long,
        val instructions: List<Long>,
    ) {
        
        fun execute(): List<Long> {
            // Initialize the output list and pointer
            val output = mutableListOf<Long>()
            var pointer = 0

            // Main execution loop
            while (pointer < instructions.size) {
                // Extract the current instruction and operand
                val opcode = instructions[pointer]
                val operand = instructions[pointer + 1]

                // Execute the instruction based on the opcode
                when (opcode) {
                    0L -> registerA = shiftLeft(operand) // adv
                    1L -> registerB = registerB xor operand // bxl
                    2L -> registerB = combo(operand) % 8 // bst
                    3L -> if (registerA != 0L) { // jnz
                        pointer = operand.toInt()
                        continue
                    }
                    4L -> registerB = registerB xor registerC // bxc
                    5L -> output.add(combo(operand) % 8) // out
                    6L -> registerB = shiftLeft(operand) // bdv
                    7L -> registerC = shiftLeft(operand) // cdv
                }

                // Increment the pointer by 2 to move to the next instruction
                pointer += 2
            }

            return output
        }

        // Helper function to perform the shift left operation
        private fun shiftLeft(operand: Long): Long {
            val power = combo(operand).toDouble()
            val denominator = 2.0.pow(power).toLong()
            return registerA / denominator
        }

        // Helper function to determine the value based on the combo operand
        private fun combo(operand: Long): Long {
            return when (operand) {
                in 0L..3L -> operand
                4L -> registerA
                5L -> registerB
                6L -> registerC
                else -> error("Invalid combo operand: $operand")
            }
        }
        
    }

    // Parse the input into a Program object
    val program = Program(
        registerA = input[0].substringAfter(": ").toLong(),
        registerB = input[1].substringAfter(": ").toLong(),
        registerC = input[2].substringAfter(": ").toLong(),
        instructions = input[4].substringAfter(": ").split(",").map { it.toLong() },
    )

    // Part 1
    program.copy().execute().joinToString(",").println()

    // Part 2
    fun Program.findRegisterAForMatchingOutput(targetOutput: List<Long>): Long {
        // Determine the starting value of registerA based on the size of the targetOutput
        var registerAStart = if (targetOutput.size == 1) {
            0L // Start from 0 if only one target instruction

        } else {
            // Recursively calculate a potential starting point for registerA
            8 * findRegisterAForMatchingOutput(targetOutput.drop(1))
        }

        // Incrementally test values of registerA until the output matches the target
        while (true) {

            // Create a copy of the current program with the updated registerA value, execute and capture the output
            val output = this.copy(registerA = registerAStart).execute()

            // If the program's output matches the target, return the current registerA value
            if (output == targetOutput) {
                return registerAStart
            }

            // Increment registerA and try again
            registerAStart++
        }
    }

    program.findRegisterAForMatchingOutput(program.instructions).println()
}
