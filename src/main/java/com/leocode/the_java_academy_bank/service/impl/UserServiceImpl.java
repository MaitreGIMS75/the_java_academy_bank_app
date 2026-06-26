package com.leocode.the_java_academy_bank.service.impl;

import com.leocode.the_java_academy_bank.dto.*;
import com.leocode.the_java_academy_bank.entity.User;
import com.leocode.the_java_academy_bank.repository.UserRepository;
import com.leocode.the_java_academy_bank.service.EmailService;
import com.leocode.the_java_academy_bank.service.UserService;
import com.leocode.the_java_academy_bank.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an Account - Saving a new user info in the database
         * Check if user already have an account
         */
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(UserUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(UserUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(UserUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);

        // Send Email Notification
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account creation")
                .messageBody("Congratulations ! Your Account Has been Successfully Created ! \n" +
                        "Your Account Details : \n" +
                        "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Account Number : " + savedUser.getAccountNumber() + " \n" +
                        "Account Balance " + savedUser.getAccountBalance())
                .attachment(null)
                .build();
        emailService.sendEmail(emailDetails);

        return BankResponse.builder()
                .responseCode(UserUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(UserUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(new AccountInfo(savedUser.getFirstName() + " "
                        + savedUser.getLastName() + " " +savedUser.getOtherName(),
                        savedUser.getAccountBalance(),
                        savedUser.getAccountNumber()))
                .build();

        // Balance Enquiry (Consultation du Solde), Name Enquiry, Credit, Debit, Transfer

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // Check if the provided Account Number Exist in the DB
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(UserUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(UserUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(UserUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(UserUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return UserUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse deposit(CreditDebitRequest request) {
        // Check if the Account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(UserUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(UserUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal newBalance = foundUser.getAccountBalance().add(request.getAmount());
        foundUser.setAccountBalance(newBalance);
        User savedUser = userRepository.save(foundUser);
        return BankResponse.builder()
                .responseCode(UserUtils.ACCOUNT_DEPOSIT_SUCCESS_CODE)
                .responseMessage(UserUtils.ACCOUNT_DEPOSIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse withdraw(CreditDebitRequest request) {
        // Check if the Account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(UserUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(UserUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // Check if the amount we want to withdraw is sufficient
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if(foundUser.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(UserUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(UserUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        BigDecimal newBalance = foundUser.getAccountBalance().subtract(request.getAmount());
        foundUser.setAccountBalance(newBalance);
        User savedUser = userRepository.save(foundUser);
        return BankResponse.builder()
                .responseCode(UserUtils.ACCOUNT_WITHDRAW_SUCCESS_CODE)
                .responseMessage(UserUtils.ACCOUNT_WITHDRAW_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // Check if debit account exist
        boolean accountToDebit = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        if(!accountToDebit){
            return BankResponse.builder()
                    .responseCode(UserUtils.DEBIT_ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(UserUtils.DEBIT_ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // Check if credit account exist
        boolean accountToCredit = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!accountToCredit){
            return BankResponse.builder()
                    .responseCode(UserUtils.CREDIT_ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(UserUtils.CREDIT_ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // Check if amount we want to transfer is sufficient on the account to debit
        User userSource = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(userSource.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(UserUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(UserUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // Debit the source account
        BigDecimal newAmount = userSource.getAccountBalance().subtract(request.getAmount());
        userSource.setAccountBalance(newAmount);
        User savedUserSource = userRepository.save(userSource);

        // Send Email Notification To the Source Account User
        EmailDetails sourceEmailDetails = EmailDetails.builder()
                .recipient(savedUserSource.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("The amount of " + request.getAmount() + " has been deducted from your account .\n" +
                        "Your current balance is " + savedUserSource.getAccountBalance())
                .attachment(null)
                .build();
        emailService.sendEmail(sourceEmailDetails);

        // Get the destination account a credit it
        User userDestination = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        BigDecimal totalAmount = userDestination.getAccountBalance().add(request.getAmount());
        userDestination.setAccountBalance(totalAmount);
        User savedUserDestination = userRepository.save(userDestination);

        // Send Email Notification To the Destination Account User
        EmailDetails destinationEmailDetails = EmailDetails.builder()
                .recipient(savedUserDestination.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("The amount of " + request.getAmount() + " has been sent to your account from " +
                        userSource.getFirstName() + " " + userSource.getLastName() + "\n"
                       + "Your current balance is " + savedUserDestination.getAccountBalance())
                .attachment(null)
                .build();
        emailService.sendEmail(destinationEmailDetails);

        return BankResponse.builder()
                .responseCode(UserUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                .responseMessage(UserUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
