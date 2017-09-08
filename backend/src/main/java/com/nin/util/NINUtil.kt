package com.nin.util


import com.nin.model.Details
import com.nin.model.Gender
import com.nin.validation.NorwegianNinValidator
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * https://en.wikipedia.org/wiki/National_identification_number#Norway
 * http://www.liquisearch.com/national_identification_number/norway
 * https://www.oecd.org/tax/automatic-exchange/crs-implementation-and-assistance/tax-identification-numbers/Norway-TIN.pdf
 */
object NINUtil {

    private val checksumSeries1 = intArrayOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 1)
    private val checksumSeries2 = intArrayOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1)

    val DATE_FORMATTER: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    private val RETRY = 10

    fun generateFakeNIN(details: Details) = generateFakeNIN(
            details.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            details.gender,
            RETRY)

    private fun generateFakeNIN(DOB: String, gender: Gender, retry: Int): String {

        val split = DOB.split("-".toRegex())

        val year = split[0]
        val month = split[1]
        val day = split[2]

        val digits = IntArray(11)

        digits[0] = Character.getNumericValue(day[0])
        digits[1] = Character.getNumericValue(day[1])
        digits[2] = Character.getNumericValue(month[0])
        digits[3] = Character.getNumericValue(month[1])
        digits[4] = Character.getNumericValue(year[2])
        digits[5] = Character.getNumericValue(year[3])

        val y = year.toInt()

        // 1854-1899 the range is 500-749.
        // 1900-1999 the range is 000-499.
        // 2000-2039 the range is 500-999.
        // 1940-1999 the range 900-999 was also used for special purposes. Not included here.
        val personalNumber =
        when(y) {
            in 1854..1899 -> ThreadLocalRandom.current().nextInt(500, 749).toString().padStart(3, '0')
            in 1900..1999 -> ThreadLocalRandom.current().nextInt(0, 499).toString().padStart(3, '0')
            in 2000..2039 -> ThreadLocalRandom.current().nextInt(500, 899).toString().padStart(3, '0')
            else -> {
               throw IllegalArgumentException("Invalid year")
            }
        }

        var genderDigit = Character.getNumericValue(personalNumber[2])

        val isRandomDigitMale = genderDigit % 2 != 0

        if (gender.isMale && !isRandomDigitMale || gender.isFemale && isRandomDigitMale) {
            genderDigit = if (genderDigit == 9) genderDigit - 1 else genderDigit + 1
        }

        digits[6] = Character.getNumericValue(personalNumber[0])
        digits[7] = Character.getNumericValue(personalNumber[1])
        digits[8] = genderDigit
        digits[9] = calculateControlDigit(checksumSeries1, digits)
        digits[10] = calculateControlDigit(checksumSeries2, digits)

        val nin = digits.joinToString(separator = "")

        // approx 10% of the time an invalid NIN is generated so add retry
        when (isValid(nin)) {
            true -> return nin
            false -> if (retry > 0) {
                return generateFakeNIN(DOB,gender,retry - 1)
            }
        }

        throw IllegalArgumentException("Could not generate NIN")
    }

    private fun isValid(nin: String) = NorwegianNinValidator.validateNorwegianNin(nin).valid
    private fun getGender(nin: String) = if (Character.getNumericValue(nin[8]) % 2 != 0) Gender.MALE else Gender.FEMALE

    private fun calculateControlDigit(checkDigitSeries: IntArray, digits: IntArray): Int {
        val sumDigits = (0 until checkDigitSeries.size).sumBy { checkDigitSeries[it] * digits[it] }

        var control = 11 - sumDigits % 11
        if (control == 11) control = 0

        return control
    }



    fun getDetails(nin: String): Details {

        val result = NorwegianNinValidator.validateNorwegianNin(nin)

        if(result.valid) {
            val gender = getGender(nin)
            val DOB = getDOB(nin)
            return Details(gender, DOB)
        }

        throw IllegalArgumentException(result.reason)
    }

    private fun getDOB(nin: String): Date {

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

        return Date.from(LocalDate.of(fullYear, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}