package com.nin.model

enum class Gender {
    MALE, FEMALE;
    val isMale: Boolean get() = this == Gender.MALE
    val isFemale: Boolean get() = this == Gender.FEMALE
}