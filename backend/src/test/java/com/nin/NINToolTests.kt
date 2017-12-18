package com.nin

import com.nin.model.Gender
import com.nin.model.GenerateRequest
import com.nin.model.NationalIdentityNumberDTO
import com.nin.util.NINUtil
import com.nin.validation.NorwegianNinValidator
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.ShouldSpec
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class NINToolTests : ShouldSpec() {
    init {

        should("generate nin") {
            val date = Date.from(LocalDate.of(1995, 3, 7).atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fakeNIN = NINUtil.generateFakeNIN(GenerateRequest(Gender.MALE, date))
            val norwegianIdNumber = NorwegianNinValidator.validateNorwegianNin(fakeNIN)
            norwegianIdNumber.valid shouldBe true
            NationalIdentityNumberDTO(fakeNIN.nationalIdentityNumber).getGender() shouldBe Gender.MALE
        }
    }
}