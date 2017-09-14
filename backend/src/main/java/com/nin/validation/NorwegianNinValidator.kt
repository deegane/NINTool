package com.nin.validation


import java.time.LocalDate

object NorwegianNinValidator {

    private val FH_NUMBER = "FHNumber"
    private val D_NUMBER = "DNumber"
    private val H_NUMBER = "HNumber"

    private val NIN_HAS_AN_INCORRECT_LENGTH = "NIN has an incorrect length."
    private val NIN_DOES_NOT_MATCH_NEEDED_REGEX = "NIN does not match needed regex."
    private val NIN_DOES_NOT_MATCH_FIRST_CHECKSUM_MODULO = "NIN does not match checksum modulo."
    private val NIN_DOES_NOT_MATCH_SECOND_CHECKSUM_MODULO = "NIN does not match checksum modulo."
    private val INVALID_BIRTH_DATE_FOR_GIVEN_NIN = "Invalid birth date for given NIN."

    private val LENGTH = 11

    // The two checksum series
    // From https://nn.wikipedia.org/wiki/F%C3%B8dselsnummer
    // And http://www.kith.no/upload/5588/KITH1001-2010_Identifikatorer-for-personer_v1.pdf

    //for digit 10, the first check digit
    private val checksumSeries1 = intArrayOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 1)
    //for digit 11, the 2nd check digit
    private val checksumSeries2 = intArrayOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1)

    private val isCorrectLength = { nin: String -> nin.length == LENGTH }
    private val matchesRegex = { nin: String -> Regex("[0-9]{2}[0,1][0-9][0-9]{2}[0-9]{5}").containsMatchIn(nin) }
    private val matchesChecksum1 = { nin: String -> matchesChecksum(nin, checksumSeries1) }
    private val matchesChecksum2 = { nin: String -> matchesChecksum(nin, checksumSeries2) }
    private val isValidBirthDate = { nin: String -> validBirthDate(nin, H_NUMBER) }

    fun validateNorwegianNin(nin: String): NinValidationResult {

        val rules = listOf(
                NinValidationStep.of("isCorrectLength", isCorrectLength, NIN_HAS_AN_INCORRECT_LENGTH),
                NinValidationStep.of("matchesRegex", matchesRegex, NIN_DOES_NOT_MATCH_NEEDED_REGEX),
                NinValidationStep.of("matchesChecksum1", matchesChecksum1, NIN_DOES_NOT_MATCH_FIRST_CHECKSUM_MODULO),
                NinValidationStep.of("matchesChecksum2", matchesChecksum2, NIN_DOES_NOT_MATCH_SECOND_CHECKSUM_MODULO),
                NinValidationStep.of("isValidBirthDate", isValidBirthDate, INVALID_BIRTH_DATE_FOR_GIVEN_NIN)
        )

        for (rule in rules) {
            try {
                val isValid = rule.predicate(nin)

                if (!isValid) {
                    print("Validation failure: $rule.predicateName")
                    return NinValidationResult.fail(rule.predicateName, rule.failReason)
                }
            } catch (e: Exception) {
                print("Failed to validate NIN: $rule.predicateName -> Exception: $e.message")
                return NinValidationResult.fail(rule.predicateName, e.message ?: "unknown")
            }

        }
        return NinValidationResult.success()
    }

    private fun matchesChecksum(nin: String, checksum: IntArray) = (0 until checksum.size).sumBy { checksum[it] * ninAsInts(nin)[it] } % 11 == 0

    private fun ninAsInts(nin: String): IntArray {
        val ninInt = IntArray(LENGTH)
        for (i in 0 until LENGTH) {
            ninInt[i] = nin[i] - '0'
        }
        return ninInt
    }

    private fun validBirthDate(nin: String, ninType: String) : Boolean {

        var day = nin.substring(0, 2).toInt()
        var month = nin.substring(2, 4).toInt()
        val year = nin.substring(4, 6).toInt()

        // D-numbers and H-numbers are encoded in the day/month field
        // Let's convert them to real dates/months
        if (day > 40) {
            if (ninType.isNotBlank() && ninType != D_NUMBER) {
                print("NIN Validation failed due to incorrect NIN type. NIN: $nin")
                return false
            }
            day -= 40
        } else if (month > 40) {
            if (ninType.isNotBlank() && ninType != H_NUMBER) {
                print("NIN Validation failed due to incorrect NIN type. NIN: $nin")
                return false
            }
            month -= 40
        }
        // FH numbers start with 8/9 and can't be used to parse years/dates out of them --> always true
        if (nin.substring(0, 1).toInt() > 7) {
            if (!ninType.isNotBlank() && nin == FH_NUMBER) {
                print("NIN Validation failed due to incorrect NIN type. NIN: $nin")
                return false
            }
            return true
        }

        // Century is encoded in the three personal digits following the date.
        val personalDigits = nin.substring(6, 9).toInt()

        val fullYear = findYearWithCentury(personalDigits, year)

        if(fullYear == 0) {
            print("NIN Validation failed due to incorrect year. NIN: {} $nin")
            return false
        }

        val isValid = !invalidDateOfBirth(day, month, fullYear)
        if (!isValid) {
            print("NIN Validation failed due to invalid date of birth. NIN: {} $nin")
        }
        return isValid
    }


    /**
     * Forwards compatible for 20+ years
     */
    private fun invalidDateOfBirth(day: Int, month: Int, year: Int) =
         year < 1854 || year > 2039 || month < 1 || month > 12 || day < 1 || day > maxDaysInMonth(year, month)

    private fun maxDaysInMonth(year: Int, month: Int) = LocalDate.of(year, month, 1).lengthOfMonth()

    // http://www.skatteetaten.no/no/Person/Folkeregister/Fodsel-og-navnevalg/Barn-fodt-i-Norge/Fodselsnummer/
    private fun findYearWithCentury(personalNumber: Int, year: Int): Int {
        return if (year >= 54 && personalNumber >= 500 && personalNumber < 750) {
            //a) … 1854-1899 -> 749-500,
            1800 + year
        } else if (personalNumber < 500) {
            //b) … 1900-1999 -> 499-000,
           1900 + year
        } else if (personalNumber >= 900 && year >= 40) {
            //c) … 1940-1999 -> 999-900
            1900 + year
        } else if (year < 40 && personalNumber >= 500) {
            //d) … 2000-2039 -> 999-500.
            2000 + year
        } else {
            // invalid
           return 0
        }
    }
}