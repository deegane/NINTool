package com.nin.validation


data class NinValidationStep (val predicateName: String, val predicate: (String) -> Boolean, val failReason: String) {

    companion object {
        fun of(predicateName: String, predicate: (String) -> Boolean, failReason: String): NinValidationStep {
            return NinValidationStep(predicateName, predicate, failReason)
        }
    }
}
