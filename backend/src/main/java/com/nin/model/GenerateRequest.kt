package com.nin.model

import com.nin.annotation.DateValid
import java.util.*

data class GenerateRequest(
        val gender: Gender,
        @DateValid(min="01-01-1854", max="31-12-2039", format="dd-MM-yyyy")
        val dob: Date)