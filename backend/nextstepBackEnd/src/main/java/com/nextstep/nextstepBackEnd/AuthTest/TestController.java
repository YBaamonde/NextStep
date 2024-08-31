package com.nextstep.nextstepBackEnd.AuthTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/protegido")
@RequiredArgsConstructor
public class TestController {

    @PostMapping(value = "test")
    public String bienvenida()
    {
        if (true) {
            return "Bienvenido a la zona protegida";
        } else {
            return "No tienes permisos para acceder a esta zona";
        }
    }
}