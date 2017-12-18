package com.nin.validation

data class NinValidationResult(val valid: Boolean, val name: String, val reason: String) {

    companion object {
        internal fun success(): NinValidationResult {
            return NinValidationResult(true, "", "")
        }

        internal fun fail(name: String, reason: String): NinValidationResult {
            return NinValidationResult(false, name, reason)
        }
    }
}
