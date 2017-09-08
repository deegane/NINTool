package com.nin.validation

data class NinValidationResult(val valid: Boolean, val name: String, val reason: String) {

    companion object {
        fun success(): NinValidationResult {
            return NinValidationResult(true, "", "")
        }

        fun fail(name: String, reason: String): NinValidationResult {
            return NinValidationResult(false, name, reason)
        }
    }
}
