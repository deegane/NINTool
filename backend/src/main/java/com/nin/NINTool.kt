package com.nin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class NINTool {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(NINTool::class.java, *args)
        }
    }
}