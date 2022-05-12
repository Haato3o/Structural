import config.StructuralProvider.formatter
import config.StructuralProvider.structuralMapper
import structural.Structural
import structural.StructuralParser
import structural.model.StructuralDefinition
import java.io.BufferedReader
import java.io.File
import java.time.LocalDate
import kotlin.system.measureTimeMillis

data class Structure(
    @Structural(size = 4)
    val id: Int,

    @Structural(size = 15)
    val hello: String,

    @Structural(size = 43)
    val message: String,

    @Structural(size = 2)
    val currency: String,

    @Structural(size = 11)
    val value: Double,

    @Structural(size = 8)
    val date: LocalDate
) : StructuralDefinition

private fun deserializeHardcodedBenchmark(structural: StructuralParser, reader: BufferedReader) {
    val timeTaken = measureTimeMillis {
        reader.lines().forEach {
            Structure(
                id = it.substring(0, 4).toInt(),
                hello = it.substring(4, 19),
                message = it.substring(19, 62),
                currency = it.substring(62, 64),
                value = it.substring(64, 75).toBigDecimal().movePointLeft(2).toDouble(),
                date = LocalDate.parse(
                    it.substring(75, 83),
                    formatter
                )
            )
        }
    }

    println("[deserializeHardcodedBenchmark] Time taken $timeTaken ms")
}

private fun deserializeRecursiveBenchmark(structural: StructuralParser, reader: BufferedReader) {
    val timeTaken = measureTimeMillis {
        reader.lines().forEach {
            structural.deserializeRecursive<Structure>(it)
        }
    }

    println("[deserializeRecursiveBenchmark] Time taken $timeTaken ms")
}

private fun deserializeRecursiveSequenceBenchmark(structural: StructuralParser, reader: BufferedReader) {
    val timeTaken = measureTimeMillis {
        reader.useLines {
            it.forEach {
                structural.deserialize<Structure>(it)
            }
        }
    }

    println("[deserializeRecursiveSequenceBenchmark] Time taken $timeTaken ms")
}

fun main(args: Array<String>) {
    val benchmarks: List<(StructuralParser, BufferedReader) -> Unit> = listOf(
        ::deserializeRecursiveBenchmark,
        ::deserializeRecursiveSequenceBenchmark,
        ::deserializeHardcodedBenchmark
    )

    benchmarks.forEach { bench ->
        for (i in 0 .. 10) {
            val file = object{}.javaClass.getResourceAsStream("/testcase.txt")!!
            val structural = StructuralParser(structuralMapper)

            bench(structural, file.bufferedReader())
        }
    }
}