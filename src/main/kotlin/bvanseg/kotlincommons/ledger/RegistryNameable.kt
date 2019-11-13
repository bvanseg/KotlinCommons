package bvanseg.kotlincommons.ledger

import bvanseg.kotlincommons.assets.KResourceLocation

/**
 * @author bright_spark
 * @since 2.1.0
 */
abstract class RegistryNameable() {
    /**
     * @return The registry name for this object
     */
    lateinit var registryName: KResourceLocation
        private set

    constructor(name: String) : this() {
        this.registryName = KResourceLocation(name)
    }

    constructor(resourceLocation: KResourceLocation) : this() {
        this.registryName = resourceLocation
    }

    //TODO: Rework this to play nice with the new KResourceLocation
//    /**
//     * Sets the domain in the [registryName] if not already set
//     *
//     * @param domain The domain to be set in the registry name
//     */
//    fun setDomain(domain: String) {
//        if (this.registryName.domain == null)
//            this.registryName.domain = domain
//    }
}

