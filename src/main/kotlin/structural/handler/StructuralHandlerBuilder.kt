package structural.handler

class StructuralHandlerBuilder {

    private val typeHandlers: MutableMap<Class<*>, (String) -> Any> = mutableMapOf()

    fun registerTypeHandler(type: Class<*>, handler: (String) -> Any): StructuralHandlerBuilder {
        typeHandlers[type] = handler

        return this
    }

    fun build(): StructuralHandler {
        return StructuralHandlerImpl(typeHandlers)
    }

}