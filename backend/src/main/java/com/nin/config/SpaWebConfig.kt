package com.nin.config

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SpaForwardingController {

    @GetMapping("/{path:[^\\.]*}", "/nin/**")
    fun forward(): String {
        return "forward:/index.html"
    }
}
