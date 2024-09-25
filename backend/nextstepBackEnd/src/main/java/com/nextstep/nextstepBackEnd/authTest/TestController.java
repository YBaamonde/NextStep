package com.nextstep.nextstepBackEnd.authTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/protegido")
@RequiredArgsConstructor
public class TestController {

    @GetMapping(value = "/test") // Cambiado a @GetMapping
    public String bienvenida() {
        return "Bienvenido a la zona protegida";
    }
}