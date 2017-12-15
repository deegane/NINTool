package com.nin.model

import java.util.*

data class BatchRequest(val gender: Gender, val from: Date, val to: Date, val numberToGenerate: Int = 1)

