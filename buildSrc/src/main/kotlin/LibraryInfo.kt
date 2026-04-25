abstract class LibraryInfo {

    protected abstract val map: Map<String, Int>
    protected abstract val flavorDimensions: List<String>
    abstract val NAME_SPACE: String

    abstract val APPLICATION_ID: String
    val VERSION_NAME
        get() = map.entries.last().key
    val VERSION_CODE
        get() = map.entries.last().value
    val FLAVOR_DIMENSION
        get() = flavorDimensions.last()
    companion object {
        const val VARIANT_INNER = "inner"
        const val VARIANT_VIVO = "vivo"
        const val VARIANT_OPPO = "oppo"
        const val VARIANT_XIAOMI = "xiaomi"
    }
}