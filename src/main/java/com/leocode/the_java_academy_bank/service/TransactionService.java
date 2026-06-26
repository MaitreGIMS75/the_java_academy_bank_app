package com.leocode.the_java_academy_bank.service;

import com.leocode.the_java_academy_bank.dto.TransactionDto;

public interface TransactionService {

    void saveTransaction(TransactionDto request);
}
