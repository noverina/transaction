package com.test.noverina.transaction.controller;

import com.test.noverina.transaction.dto.HttpResponseDto;
import com.test.noverina.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService service;

    @Operation(summary = "get all transaction for a given account in a given year and month")
    @GetMapping
    public ResponseEntity<HttpResponseDto<?>> findAll(@RequestParam String accountId, @Parameter(description = "in number, e.g December -> input 12") @RequestParam int month, @RequestParam int year, @Parameter(description = "first page is 1 (not 0)") @RequestParam int page, @RequestParam int pageSize) throws AccessDeniedException {
        var result = service.findAll(accountId, month, year, page, pageSize);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}
