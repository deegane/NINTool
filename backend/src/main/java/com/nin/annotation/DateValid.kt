package com.nin.annotation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.FILE)
@Constraint(validatedBy = [DateValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DateValid(val message: String = "Invalid Date",
                           val groups: Array<KClass<*>> = [],
                           val payload: Array<KClass<out Payload>> = [],
                           val min: String,
                           val max: String,
                           val format: String)