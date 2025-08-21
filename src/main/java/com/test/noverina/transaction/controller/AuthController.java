package com.test.noverina.transaction.controller;

import com.test.noverina.transaction.dto.AuthDto;
import com.test.noverina.transaction.dto.HttpResponseDto;
import com.test.noverina.transaction.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping
    public ResponseEntity<HttpResponseDto<?>> auth(@RequestBody AuthDto dto)  {
        var token = service.auth(dto);
        var httpRes = new HttpResponseDto<>(false, "", token);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}