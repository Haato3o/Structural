package structural.handler

interface StructuralHandler {
    fun handle(type: Class<*>, rawValue: String): Any
}