package com.nin.annotation

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DateValidator : ConstraintValidator<DateValid, Date> {

    lateinit var min: String
    lateinit var max: String
    lateinit var format: String

    override fun initialize(obj: DateValid) {
        this.min = obj.min
        this.max = obj.max
        this.format = obj.format
    }

    override fun isValid(date: Date, context: ConstraintValidatorContext): Boolean {

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)

        val minDate = LocalDate.parse(min, dateFormatter)
        val maxDate = LocalDate.parse(max, dateFormatter)

        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (!localDate.isBefore(maxDate) || !localDate.isAfter(minDate)) {
            throw IllegalArgumentException("Invalid date provided")
        }
        return true
    }
}