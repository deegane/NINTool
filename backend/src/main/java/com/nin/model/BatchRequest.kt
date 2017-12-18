package com.nin.model

import com.nin.annotation.DateValid
import java.util.*

data class BatchRequest(
        val gender: Gender,
        @DateValid(min="01-01-1854", max="31-12-2039", format="dd-MM-yyyy")
        val from: Date,
        @DateValid(min="01-01-1854", max="31-12-2039", format="dd-MM-yyyy")
        val to: Date,
        val numberToGenerate: Int = 1)