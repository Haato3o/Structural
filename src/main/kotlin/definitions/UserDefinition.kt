package definitions

import structural.Structural
import structural.model.StructuralDefinition
import java.time.LocalDate

data class UserDefinition(
    @Structural(size = 8)
    val id: Long,

    @Structural(size = 32)
    val username: String,

    @Structural(size = 8)
    val createdAt: LocalDate,

    @Structural(size = 11)
    val balance: Double
) : StructuralDefinition
