package com.nin.validation

import com.nin.model.Gender
import com.nin.model.NationalIdentityNumber
import com.nin.model.NationalIdentityNumberDTO
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.ShouldSpec

class NorwegianNinValidatorTest : ShouldSpec() {

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

    init {

        should("generate male nins") {
            males.forEach {
                val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber(it, Gender.MALE))
                result.valid shouldBe true
                NationalIdentityNumberDTO(it).getGender() shouldBe Gender.MALE
            }
        }

        should("generate female nins") {
            females.forEach {
                val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber(it, Gender.FEMALE))
                result.valid shouldBe true
                NationalIdentityNumberDTO(it).getGender() shouldBe Gender.FEMALE
            }
        }

        should ("fail checksum check") {
            invalidControlDigits.forEach {
                val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber(it))
                result.valid shouldBe false
                result.reason shouldBe "NIN does not match checksum modulo."
            }
        }

        should("fail length check") {
            val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber("12344556"))
            result.valid shouldBe false
            result.reason shouldBe "NIN has an incorrect length."
        }

        should("fail regex check") {
            val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber("123456789AB"))
            result.valid shouldBe false
            result.reason shouldBe "NIN does not match needed regex."
        }

        should("fail birthday range check") {
            val result = NorwegianNinValidator.validateNorwegianNin(NationalIdentityNumber("17029949266"))
            result.valid shouldBe false
            result.reason shouldBe "NIN does not match checksum modulo."
        }
    }
}