package com.nin.controller

import com.nin.model.BatchRequest
import com.nin.model.Details
import com.nin.util.NINUtil
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"]) // left in dev purposes
class Controller {

    @PostMapping("/generate")
    fun generateFakeNIN(@RequestBody details: Details) = NINUtil.generateFakeNIN(details)

    @PostMapping(value = ["/batch"],
                produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun generateBatch(@RequestBody batch: BatchRequest) : ResponseEntity<Resource>
            = ResponseEntity(FileSystemResource(NINUtil.generateBatch(batch)), HttpStatus.OK)

    @PostMapping("/validate")
    fun generateFakeNIN(@RequestBody nin: List<String>) = NINUtil.details(nin[0])

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun handleIllegalArgument(e : IllegalArgumentException) = e.message ?: "unknown error"
}