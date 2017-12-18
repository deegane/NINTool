package com.nin.util


import com.nin.model.BatchRequest
import com.nin.model.Gender
import com.nin.model.GenerateRequest
import com.nin.model.NationalIdentityNumber
import com.nin.validation.NorwegianNinValidator
import java.io.BufferedWriter
import java.io.File
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom

/**
 * https://en.wikipedia.org/wiki/National_identification_number#Norway
 * http://www.liquisearch.com/national_identification_number/norway
 * https://www.oecd.org/tax/automatic-exchange/crs-implementation-and-assistance/tax-identification-numbers/Norway-TIN.pdf
 */
object NINUtil {

    private val checksumSeries1 = intArrayOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 1)
    private val checksumSeries2 = intArrayOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1)

    private const val RETRY = 10

    val DATE_FORMATTER: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun generateFakeNIN(generateRequest: GenerateRequest) = generateFakeNIN(
            generateRequest.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            generateRequest.gender,
            RETRY)

    fun generateBatch(batch: BatchRequest) : File {

        val ninList = generateList(batch)

        val file = File.createTempFile("tmp", ".txt")

        file.bufferedWriter().use {
            out -> ninList.forEachIndexed { i, nin -> out.writeLn(nin, i, batch.numberToGenerate - 1)}
        }

        return file
    }

    private fun BufferedWriter.writeLn(line: String, current: Int, size: Int) {
        this.write(line)
        if(size!=current) {
            this.newLine()
        }
    }

    private fun generateList(batch: BatchRequest) : Set<String> {
        val ninList = mutableSetOf<String>()
        val fromDate = batch.from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val toDate = batch.to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if(fromDate.isAfter(toDate)) {
            throw IllegalArgumentException("From date must be after To date")
        }

        val days = ChronoUnit.DAYS.between(fromDate, toDate)

        while(ninList.size < batch.numberToGenerate) {

            val randomDate = fromDate.plusDays(ThreadLocalRandom.current().nextLong(days+1))

            val nin = generateFakeNIN(
                    randomDate.toString(),
                    batch.gender,
                    RETRY)

            ninList.add(nin.nationalIdentityNumber)
        }

       return ninList
    }

    private fun generateFakeNIN(DOB: String, gender: Gender, retry: Int): NationalIdentityNumber {

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
        when (valid(nin, gender)) {
            true -> return NationalIdentityNumber(nin, gender)
            false -> if (retry > 0) {
                return generateFakeNIN(DOB,gender,retry - 1)
            }
        }

        throw IllegalArgumentException("Could not generate NIN")
    }

    private fun valid(nin: String, gender: Gender) = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber(nin, gender)).valid

    private fun calculateControlDigit(checkDigitSeries: IntArray, digits: IntArray): Int {
        val sumDigits = (0 until checkDigitSeries.size).sumBy { checkDigitSeries[it] * digits[it] }
        val control = 11 - sumDigits % 11
        return if (control == 11) 0 else control
    }
}