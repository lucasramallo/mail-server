package com.github.mailserver.controller;

import com.github.mailserver.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private SenderService service;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail() {
        service.sendEmail("lucasramalho.dev@gmail.com", "Jonh Doe");

        return ResponseEntity.ok("done");
    }
}
