package com.nin.model

import com.nin.annotation.NinValid
import jakarta.validation.constraints.NotEmpty

@NinValid
data class NationalIdentityNumber(@field:NotEmpty val nationalIdentityNumber: String, var gender: Gender = Gender.UNKNOWN)