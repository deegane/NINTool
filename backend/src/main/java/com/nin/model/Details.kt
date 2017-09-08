package com.nin.model

import com.nin.util.NINUtil
import java.time.ZoneId
import java.util.*

data class Details(val gender: Gender, val dob: Date) {
    var formattedDOB = NINUtil.DATE_FORMATTER?.format(dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
}