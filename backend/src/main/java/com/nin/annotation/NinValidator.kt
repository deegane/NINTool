package com.nin.annotation

import com.nin.model.NationalIdentityNumber
import com.nin.validation.NorwegianNinValidator
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NinValidator : ConstraintValidator<NinValid, NationalIdentityNumber> {
    override fun isValid(nin: NationalIdentityNumber, context: ConstraintValidatorContext): Boolean {
        val ninCheck = NorwegianNinValidator.validateNorwegianNin(nin)
        if(!ninCheck.valid) {
            throw IllegalArgumentException(ninCheck.reason)
        }
        return true
    }
}