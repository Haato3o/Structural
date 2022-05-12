package config

import structural.handler.StructuralHandlerBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object StructuralProvider {
    val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")

    val structuralMapper = StructuralHandlerBuilder()
        .registerTypeHandler(Int::class.java) { raw ->
            raw.toInt()
        }
        .registerTypeHandler(Long::class.java) { raw ->
            raw.toLong()
        }
        .registerTypeHandler(Double::class.java) { raw ->
            raw.toBigDecimal()
                .movePointLeft(2)
                .toDouble()
        }
        .registerTypeHandler(LocalDate::class.java) { raw ->
            LocalDate.parse(raw, formatter)
        }
        .registerTypeHandler(String::class.java) { raw ->
            raw
        }
        .build()
}