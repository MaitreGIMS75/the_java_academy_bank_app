package com.leocode.the_java_academy_bank.controller;

import com.leocode.the_java_academy_bank.dto.*;
import com.leocode.the_java_academy_bank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "User Account Management APIs")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating New User and Assigning an Account Id"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status Code CREATED"
    )
    @PostMapping
    public ResponseEntity<BankResponse> createdAccount(@RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createAccount(userRequest));
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given an account number, Check how the User has"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status Code OK"
    )
    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.balanceEnquiry(request));
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Given an account number, Check the account owner"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status Code OK"
    )
    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> nameEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.nameEnquiry(request));
    }

    @Operation(
            summary = "Deposit",
            description = "Credited an Account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status Code OK"
    )
    @PostMapping("/deposit")
    public ResponseEntity<BankResponse> deposit(@RequestBody CreditDebitRequest request){
        return ResponseEntity.ok(userService.deposit(request));
    }

    @Operation(
            summary = "Withdraw",
            description = "Debited an Account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status Code OK"
    )
    @PostMapping("/withdraw")
    public ResponseEntity<BankResponse> withdraw(@RequestBody CreditDebitRequest request){
        return ResponseEntity.ok(userService.withdraw(request));
    }

    @Operation(
            summary = "Transfer",
            description = "Transfer to one Account to another Account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status Code OK"
    )
    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transfer(@RequestBody TransferRequest request){
        return ResponseEntity.ok(userService.transfer(request));
    }
}
