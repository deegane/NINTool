package com.nin.model

import com.nin.annotation.NinValid
import javax.validation.constraints.NotEmpty

@NinValid
data class NationalIdentityNumber(@NotEmpty val nationalIdentityNumber: String, var gender: Gender = Gender.UNKNOWN)