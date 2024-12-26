package me.daegyeo.maru.auth.adaptor.`in`.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Controller
@RestController
@RequestMapping("/auth")
class AuthController {
    @GetMapping("/me")
    fun getMyInfo(): String {
        return "Hello, World!"
    }

    @PostMapping("/login")
    fun login(): String {
        return "Hello, World!"
    }

    @DeleteMapping("/logout")
    fun logout(): String {
        return "Hello, World!"
    }
}
