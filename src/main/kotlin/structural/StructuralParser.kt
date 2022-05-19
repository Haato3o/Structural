package structural

import structural.exceptions.ClassConstructorNotFound
import structural.exceptions.StructureSizeIsTooBig
import structural.handler.StructuralHandler
import structural.model.StructuralDefinition
import java.lang.reflect.Field

class StructuralParser(
    val structuralHandler: StructuralHandler
) {

    tailrec fun deserialize(
        content: String,
        fields: Array<Field>,
        index: Int,
        currentOffset: Int,
        values: List<Any>
    ): Array<Any> {
        return when(index < fields.size) {
            true -> {
                val field = fields[index]
                val structuralAttribute = field.getAnnotation(Structural::class.java)
                val endOffset = currentOffset + structuralAttribute.size

                if (endOffset > content.length)
                    throw StructureSizeIsTooBig()

                val rawValue = content.substring(currentOffset, endOffset).trim()
                val value = structuralHandler.handle(field.type, rawValue)

                deserialize(content, fields, index + 1, endOffset, values + value)
            }
            else -> values.toTypedArray()
        }
    }

    inline fun <reified T> deserializeRecursive(
        content: String,
        offset: Int = 0
    ) where T : StructuralDefinition =
        T::class.java.declaredFields.let { fields ->
            deserialize(content, fields, 0, offset, arrayListOf()).run {
                T::class.constructors.find { it.parameters.size == this.size }?.call(*this)
                    ?: throw ClassConstructorNotFound()
            }
        }

    inline fun <reified T> deserialize(
        content: String,
        offset: Int = 0
    ) where T : StructuralDefinition {
        T::class.java.declaredFields.let { fields ->
            var cursor = offset
            Array(fields.size) { index ->
                val field = fields[index]
                val structuralAttribute = field.getAnnotation(Structural::class.java)
                val endOffset = cursor + structuralAttribute.size

                if (endOffset > content.length)
                    throw StructureSizeIsTooBig()

                val rawValue = content.substring(cursor, endOffset).trim()
                cursor = endOffset
                structuralHandler.handle(field.type, rawValue)
            }.also { params ->
                T::class.constructors.find { it.parameters.size == params.size }?.call(*params)
                    ?: throw ClassConstructorNotFound()
            }
        }
    }
}