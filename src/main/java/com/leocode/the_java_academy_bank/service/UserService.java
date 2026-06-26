package com.leocode.the_java_academy_bank.service;

import com.leocode.the_java_academy_bank.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse deposit(CreditDebitRequest request);

    BankResponse withdraw(CreditDebitRequest request);

    BankResponse transfer(TransferRequest request);
}
