package com.nin.controller

import com.nin.model.BatchRequest
import com.nin.model.GenerateRequest
import com.nin.model.NationalIdentityNumber
import com.nin.model.NationalIdentityNumberDTO
import com.nin.util.NINUtil
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["*"]) // left in dev purposes
class Controller {

    @PostMapping("/generate")
    fun generateFakeNIN(@Valid @RequestBody generateRequest: GenerateRequest) = NINUtil.generateFakeNIN(generateRequest)

    @PostMapping(value = ["/batch"],
                produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun generateBatch(@Valid @RequestBody batch: BatchRequest) = ResponseEntity(
            FileSystemResource(NINUtil.generateBatch(batch)),
            HttpStatus.OK
    )

    @PostMapping("/validate")
    fun validate(@Valid @RequestBody nin: NationalIdentityNumber): NationalIdentityNumberDTO {
       return NationalIdentityNumberDTO(nin.nationalIdentityNumber)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun handleIllegalArgument(e : IllegalArgumentException) = e.message ?: "unknown error"
}