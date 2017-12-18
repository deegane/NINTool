package com.nin.validation

import com.nin.model.NationalIdentityNumber


data class NinValidationStep (val predicateName: String, val predicate: (NationalIdentityNumber) -> Boolean, val failReason: String) {

    companion object {
        fun of(predicateName: String, predicate: (NationalIdentityNumber) -> Boolean, failReason: String): NinValidationStep {
            return NinValidationStep(predicateName, predicate, failReason)
        }
    }
}