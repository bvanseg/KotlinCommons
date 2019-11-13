package bvanseg.kotlincommons.assets

import bvanseg.kotlincommons.assets.KResource.Companion.defaultRoot
import bvanseg.kotlincommons.string.Textualizer

class KActor(override val domain: String, override val location: String): KResource {

    override val path: String = "$defaultRoot$domain/$location"

    override fun toResourceMode(): KResourceLocation = KResourceLocation(domain, location)
    override fun toActorMode(shouldClose: Boolean): KActor = this

    override fun toString(): String = Textualizer.builder(this)
        .append("defaultRoot", defaultRoot)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()
}