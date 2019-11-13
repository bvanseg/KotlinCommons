package bvanseg.kotlincommons.assets

interface KResource {

    val domain: String
    val location: String
    val path: String

    fun toResourceMode(): KResourceLocation
    fun toActorMode(shouldClose: Boolean = false): KActor

    companion object {
        @JvmStatic
        var defaultRoot = "assets/"
    }
}