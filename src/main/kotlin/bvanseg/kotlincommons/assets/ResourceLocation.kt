package bvanseg.kotlincommons.assets

import org.apache.commons.lang3.builder.EqualsBuilder
import java.io.InputStream

class ResourceLocation {

    var domain: String? = null

    var location: String? = null
        private set

    val path: String
        get() = "/$baseFolder/" + this.domain + "/" + this.location

    val inputStream: InputStream

    constructor(location: String) {
        val resourceLocationRaw = location.split(":".toRegex(), 2).toTypedArray()
        if (resourceLocationRaw.size > 1) {
            this.domain = resourceLocationRaw[0]
            this.location = resourceLocationRaw[1]
        } else {
            this.domain = defaultDomain
            this.location = location
        }
        inputStream = Resources.scan.getResourcesWithPath(path).first().open()
    }

    constructor(domain: String, location: String) {
        this.domain = domain
        this.location = location
        inputStream = Resources.scan.getResourcesWithPath(path).first().open()
    }

    constructor(folder: ResourceLocation, location: String) {
        this.location = folder.domain + ":" + folder.location + "/" + location
        inputStream = Resources.scan.getResourcesWithPath(path).first().open()
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + if (this.domain == null) 0 else this.domain!!.hashCode()
        result = 31 * result + if (this.location == null) 0 else this.location!!.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (this === other) {
            return true
        }
        if (this.javaClass != other.javaClass) {
            return false
        }
        val o = other as ResourceLocation?
        return EqualsBuilder()
            .append(this.domain, o!!.domain)
            .append(this.location, o.location)
            .isEquals
    }

    override fun toString(): String {
        return this.domain + ":" + this.location
    }

    companion object {
        @JvmStatic
        var defaultDomain: String = "base"
        @JvmStatic
        var baseFolder: String = "assets"
    }
}
