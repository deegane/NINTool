package com.nin.controller

import com.nin.model.Details
import com.nin.util.NINUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = arrayOf("*")) // left in dev purposes
class Controller {

    @PostMapping("/generate")
    fun generateFakeNIN(@RequestBody details: Details) = NINUtil.generateFakeNIN(details)

    @PostMapping("/validate")
    fun generateFakeNIN(@RequestBody nin: List<String>) = NINUtil.details(nin[0])

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun handleIllegalArgument(e : IllegalArgumentException) = e.message ?: "unknown error"
}