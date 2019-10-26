package bvanseg.kotlincommons.kclasses

import kotlin.reflect.KClass
import kotlin.reflect.KType


fun KType.getKClass(): KClass<*> = this.classifier!! as KClass<*>