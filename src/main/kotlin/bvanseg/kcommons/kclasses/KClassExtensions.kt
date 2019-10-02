package bvanseg.kcommons.kclasses

import kotlin.reflect.KClass
import kotlin.reflect.KType


fun KType.getKClass(): KClass<*> = this.classifier!! as KClass<*>