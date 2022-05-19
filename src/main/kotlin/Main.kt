import benchmark.BenchmarkHelper
import benchmark.WarmUp
import config.StructuralProvider.formatter
import config.StructuralProvider.structuralMapper
import structural.Structural
import structural.StructuralParser
import structural.model.StructuralDefinition
import java.io.BufferedReader
import java.time.LocalDate

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

private fun deserializeForLoopSequenceBenchmark(structural: StructuralParser, reader: BufferedReader) {
    reader.useLines {
        it.forEach {
            structural.deserialize<Structure>(it)
        }
    }
}

private fun deserializeRecursiveBenchmark(structural: StructuralParser, reader: BufferedReader) {
    reader.lines().forEach {
        structural.deserializeRecursive<Structure>(it)
    }
}

private fun deserializeRecursiveSequenceBenchmark(structural: StructuralParser, reader: BufferedReader) {
    reader.useLines {
        it.forEach {
            structural.deserializeRecursive<Structure>(it)
        }
    }
}

fun main(args: Array<String>) {

    WarmUp.run()

    val benchmarks: List<(StructuralParser, BufferedReader) -> Unit> = listOf(
        ::deserializeRecursiveBenchmark,
        ::deserializeForLoopSequenceBenchmark,
        ::deserializeRecursiveSequenceBenchmark,
        ::deserializeHardcodedBenchmark,

    )
    val structural = StructuralParser(structuralMapper)
    val benchy = BenchmarkHelper("/testcase.txt", structural)

    benchmarks.forEach { bench ->
        benchy.run(bench)
    }
}