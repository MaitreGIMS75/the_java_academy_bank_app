package com.leocode.the_java_academy_bank.service.impl;

import com.leocode.the_java_academy_bank.dto.TransactionDto;
import com.leocode.the_java_academy_bank.entity.Transaction;
import com.leocode.the_java_academy_bank.repository.TransactionRepository;
import com.leocode.the_java_academy_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionImpl implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .accountNumber(transactionDto.getAccountNumber())
                .status("SUCCESS")
                .build();
        repository.save(transaction);
        System.out.println("Transaction Save Successfully");
    }
}
