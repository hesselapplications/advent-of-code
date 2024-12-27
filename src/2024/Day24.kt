fun main() {
    // Read input
    val input = readInput("2024/Day24").joinToString("\n")

    // Store gate inputs, operation, and output
    data class Gate(
        val a: String,
        val op: String,
        val b: String,
        var c: String,
    )

    // Parse wires from input
    val wires = Regex("""(\w+): (\d)""").findAll(input).associate { match ->
        val (name, value) = match.destructured
        name to value.toIntOrNull()
    }.toMutableMap()

    // Parse gates from input
    val gates = Regex("""(\w+) (AND|OR|XOR) (\w+) -> (\w+)""").findAll(input).map { match ->
        val (a, op, b, c) = match.destructured
        Gate(a, op, b, c)
    }.toList()

    val AND = "AND"
    val OR = "OR"
    val XOR = "XOR"

    // Part 1
    while (true) {
        // Find gates that are ready to compute
        val readyToCompute = gates.filter {
            wires[it.a] != null && wires[it.b] != null && wires[it.c] == null
        }

        // If no gates are ready to compute, we are done
        if (readyToCompute.isEmpty()) break

        // Compute the output of the gates
        readyToCompute.forEach { gate ->
            val firstSignal = wires[gate.a]!!
            val secondSignal = wires[gate.b]!!

            wires[gate.c] = when (gate.op) {
                AND -> firstSignal and secondSignal
                OR -> firstSignal or secondSignal
                XOR -> firstSignal xor secondSignal
                else -> error("Unknown operation: ${gate.op}")
            }
        }
    }

    // Print the output of the last gate
    wires.entries
        .filter { it.key.startsWith("z") } // Find all "z" wires
        .sortedByDescending { it.key } // Sort them in descending order
        .map { it.value } // Get the bit value of each wire
        .joinToString("") // Join to a single string
        .toLong(2) // Convert the binary string to a decimal long
        .println()


    // Part 2
    fun Gate.isValid(): Boolean {
        val nextOps = gates
            .filter { it.a == c || it.b == c } // Find all gates that use this gate's output
            .map { it.op } // Get the operation of each gate
            .toSet() // Collect the operations into a set

        return when(op) {
            // First input AND || other input ANDs / carry ANDs
            AND -> setOf("x00", "y00") == setOf(a, b) || nextOps == setOf(OR)

            // Input XORs || output XORs
            XOR -> a.first() in "xy" && nextOps == setOf(XOR, AND) || nextOps.isEmpty()

            // Carry ORs || final carry OR
            OR -> nextOps == setOf(XOR, AND) || c == "z45"

            // Invalid
            else -> false
        }
    }

    gates
        .filter { !it.isValid() } // Find all invalid gates
        .map { it.c } // Get the output of each invalid gate
        .sorted() // Sort the outputs
        .joinToString(",") // Join to a single string
        .println()
}