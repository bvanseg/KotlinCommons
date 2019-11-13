package bvanseg.kotlincommons.assets

import bvanseg.kotlincommons.assets.KResource.Companion.defaultRoot
import bvanseg.kotlincommons.string.Textualizer
import java.io.File
import java.io.InputStream

class KResourceLocation: KResource, AutoCloseable {

    override var domain: String = ""
        private set
    override var location: String = ""
        private set

    override var path: String = ""
        private set

    constructor(domain: String, location: String) {
        this.domain = domain
        this.location = location
        this.path = "$defaultRoot$domain/$location"
    }

    constructor(parent: KResourceLocation) {
        this.domain = parent.domain
        this.location = parent.location
        this.path = "$defaultRoot$domain/$location"
    }

    constructor(location: String) {
        val resourceLocationRaw = location.split(":".toRegex(), 2).toTypedArray()
        if (resourceLocationRaw.size > 1) {
            this.domain = resourceLocationRaw[0]
            this.location = resourceLocationRaw[1]
        } else {
            this.domain = defaultRoot
            this.location = location
        }
        this.path = "$domain/$location"
    }

    private val resource by lazy { Resources.scan.getResourcesWithPath(path).first() }

    val inputStream: InputStream by lazy { resource.open() }
    val file: File by lazy { resource.classpathElementFile }

    override fun toResourceMode(): KResourceLocation = this

    override fun toActorMode(shouldClose: Boolean): KActor {
        if(shouldClose) close()
        return KActor(domain, location)
    }

    override fun close() = inputStream.close()

    override fun toString(): String = Textualizer.builder(this)
        .append("defaultRoot", defaultRoot)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()
}