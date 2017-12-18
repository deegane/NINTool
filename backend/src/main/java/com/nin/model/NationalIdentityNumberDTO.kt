package com.nin.model


import com.nin.util.NINUtil
import java.time.LocalDate

private const val GENDER_DIGIT = 8

data class NationalIdentityNumberDTO(private val nin: String) {

    fun getGender(): Gender {
      return if (Character.getNumericValue(nin[GENDER_DIGIT]) % 2 != 0) Gender.MALE else Gender.FEMALE
    }

    fun getDob(): String? {

        val day = nin.substring(0, 2).toInt()
        val month = nin.substring(2, 4).toInt()
        val year = nin.substring(4, 6).toInt()
        val personalDigits = nin.substring(6, 9).toInt()

        val fullYear = if (personalDigits < 500) {
            year + 1900
        } else if (personalDigits < 750 && year >= 54) {
            year + 1800
        } else if (year < 40) {
            year + 2000
        } else {
            year + 1900
        }

        return NINUtil.DATE_FORMATTER?.format(LocalDate.of(fullYear, month, day))
    }
}