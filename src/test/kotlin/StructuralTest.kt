import config.StructuralProvider.structuralMapper
import definitions.UserDefinition
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import structural.StructuralParser
import structural.exceptions.InvalidValueForType
import structural.exceptions.StructureSizeIsTooBig
import java.time.LocalDate

class StructuralTest : ShouldSpec({

    val structural = StructuralParser(
        structuralHandler = structuralMapper
    )

    should("turn a string into a data class correctly") {
        val testCase = "00124758Haato Testing                   0905202200000352100"

        structural.deserialize<UserDefinition>(testCase).run {
            id shouldBeExactly 124758
            username shouldBe "Haato Testing"
            createdAt shouldBe LocalDate.of(2022, 5, 9)
            balance shouldBe 3521.00
        }
    }

    should("throw error when the structure is bigger than given string") {
        val testCase = "00124758"

        shouldThrow<StructureSizeIsTooBig> { structural.deserialize<UserDefinition>(testCase) }
    }

    should("throw error when deserializing wrong value in the wrong type") {
        val testCase = "string00abcdef"

        shouldThrow<InvalidValueForType> { structural.deserialize<UserDefinition>(testCase) }
    }
})