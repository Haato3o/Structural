package benchmark

import java.io.BufferedReader
import kotlin.system.measureTimeMillis
import structural.StructuralParser

class BenchmarkHelper(
    private val testcaseFilename: String,
    private val structuralParser: StructuralParser
) {

    private fun runFunction(func: (StructuralParser, BufferedReader) -> Unit): List<Long> {
        val times = mutableListOf<Long>()
        for (i in 1 .. 10) {
            val reader = javaClass.getResourceAsStream(testcaseFilename)!!.bufferedReader()

            times.add(measureTimeMillis {
                func(structuralParser, reader)
            })
        }

        return times
    }

    private fun warmUp(func: (StructuralParser, BufferedReader) -> Unit) {
        runFunction(func)
    }

    fun run(func: (StructuralParser, BufferedReader) -> Unit) {
        warmUp(func)

        val times = runFunction(func)

        val totalTime = times.sumOf { it }
        val avgTime = totalTime / times.size
        val maxTime = times.maxOf { it }
        val minTime = times.minOf { it }

        println("Results for ${func}:\n" +
                "- Total:   $totalTime ms\n" +
                "- Avg:     $avgTime ms\n" +
                "- Max:     $maxTime ms\n" +
                "- Min:     $minTime ms\n")
    }

}