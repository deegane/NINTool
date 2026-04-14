package com.nin.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Constraint(validatedBy = [NinValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NinValid(val message: String = "Invalid NIN",
                          val groups: Array<KClass<*>> = [],
                          val payload: Array<KClass<out Payload>> = [])