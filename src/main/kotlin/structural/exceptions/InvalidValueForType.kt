package structural.exceptions

class InvalidValueForType(
    private val type: Class<*>,
    private val value: String
) : Exception("Value '$value' is invalid for type ${type.typeName}")