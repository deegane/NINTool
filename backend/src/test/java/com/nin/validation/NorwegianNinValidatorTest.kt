package com.nin.validation

import com.nin.util.NINUtil
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest
class NorwegianNinValidatorTest {

     private var males = listOf("21034814500",
            "26077938760",
            "29011288160",
            "08092135560",
            "02041639574",
            "25067148161",
            "04041502989",
            "15080779728",
            "06054332392",
            "15118313930",
            "17024749347",
            "17101011372",
            "02033549159",
            "12028626795",
            "25080199980",
            "22043700778",
            "16094805124",
            "10010289942",
            "15078102397",
            "05048019104",
            "04097947576",
            "06010320184",
            "28038208197",
            "01092127121",
            "11049532360",
            "10056720729",
            "06033316370",
            "28024210385",
            "27089229117",
            "18070472976",
            "28122418938",
            "28044231346",
            "12046504923")

     private var females = listOf(
            "01092127040",
            "27123326615",
            "16070870618",
            "02041639493",
            "25067148080",
            "08060618217",
            "23055438218",
            "15080779647",
            "25096014859",
            "06054332201",
            "16121941299",
            "17024749266",
            "17101011291",
            "02033549078",
            "12028626604",
            "22043700697",
            "16094805043",
            "10010289861",
            "15078102206",
            "05048019023",
            "04097947495",
            "28038208006",
            "10056720648",
            "27089229036",
            "18070472895",
            "28122418857",
            "13128045818",
            "28044231265",
            "12046504842",
            "22045034627",
            "25061041608",
            "16031021409",
            "08023725097",
            "25085523085",
            "30097320280",
            "15038302601",
            "13100669441"
    )

    private val invalidControlDigits = listOf(
            "18099805991",
            "53124717928"
    )


    @Test
    fun test() {
        val result = NorwegianNinValidator.validateNorwegianNin("25091772739")
        print(result)
        Assert.assertTrue(result.valid)
    }

    @Test
    fun males() {
        males.forEach {
            assertTrue(NorwegianNinValidator.validateNorwegianNin(it).valid)
            assertTrue(NINUtil.details(it).gender.isMale)
        }
    }

    @Test
    fun females() {
        females.forEach {
            assertTrue(NorwegianNinValidator.validateNorwegianNin(it).valid)
            assertTrue(NINUtil.details(it).gender.isFemale)
        }
    }

    @Test
    fun invalidControlDigits() {
        invalidControlDigits.forEach {
            val validationResult = NorwegianNinValidator.validateNorwegianNin(it)
            assertFalse(validationResult.valid)
            assertEquals(validationResult.reason, "NIN does not match checksum modulo.")
        }
    }

    @Test
    fun incorrectLength() {
        val validationResult = NorwegianNinValidator.validateNorwegianNin("12344556")
        assertFalse(validationResult.valid)
        assertEquals(validationResult.reason, "NIN has an incorrect length.")
    }

    @Test
    fun regex() {
        val validationResult = NorwegianNinValidator.validateNorwegianNin("123456789AB")
        assertFalse(validationResult.valid)
        assertEquals(validationResult.reason, "NIN does not match needed regex.")
    }

    @Test
    fun birthdayNotInRange() {
        val validationResult = NorwegianNinValidator.validateNorwegianNin("17029949266")
        assertFalse(validationResult.valid)
        assertEquals(validationResult.reason, "NIN does not match checksum modulo.")
    }
}