package com.nin

import com.nin.model.Details
import com.nin.model.Gender
import com.nin.util.NINUtil
import com.nin.validation.NorwegianNinValidator
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class NINToolTests {

    @Test
    fun generateNIN() {
        val date = Date.from(LocalDate.of(1995, 3, 7).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val fakeNIN = NINUtil.generateFakeNIN(Details(Gender.MALE, date))
        val norwegianIdNumber = NorwegianNinValidator.validateNorwegianNin(fakeNIN)
        assertTrue(norwegianIdNumber.valid)
        assertTrue(NINUtil.details(fakeNIN).gender.isMale)
    }
}