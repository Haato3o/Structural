package structural.handler

import structural.exceptions.InvalidValueForType

internal class StructuralHandlerImpl(
    private val typeHandlers: MutableMap<Class<*>, (String) -> Any> = mutableMapOf()
) : StructuralHandler {

    override fun handle(type: Class<*>, rawValue: String) =
        typeHandlers[type]?.runCatching {
            this(rawValue)
        }?.getOrNull() ?: throw InvalidValueForType(type, rawValue)

}