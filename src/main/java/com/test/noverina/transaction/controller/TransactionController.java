package com.test.noverina.transaction.controller;

import com.test.noverina.transaction.dto.HttpResponseDto;
import com.test.noverina.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService service;

    @GetMapping
    public ResponseEntity<HttpResponseDto<?>> findAll(@RequestParam String userId, @RequestParam int month, @RequestParam int year, @RequestParam int page, @RequestParam int pageSize)  {
        var result = service.findAll(userId, month, year, page, pageSize);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}
