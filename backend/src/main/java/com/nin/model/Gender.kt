package com.nin.model

enum class Gender {
    MALE, FEMALE, UNKNOWN;
    val isMale: Boolean get() = this == Gender.MALE
    val isFemale: Boolean get() = this == Gender.FEMALE
    val isUnknown: Boolean get() = this == Gender.UNKNOWN
}