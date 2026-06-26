package com.leocode.the_java_academy_bank.utils;

import java.time.Year;

public class UserUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "A USER ALREADY HAS AN ACCOUNT CREATED !";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "ACCOUNT HAS BEEN SUCCESSFULLY CREATED";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "USER WITH PROVIDED ACCOUNT NUMBER DOESNT EXIST";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "USER ACCOUNT FOUND";

    public static final String ACCOUNT_DEPOSIT_SUCCESS_CODE = "005";
    public static final String ACCOUNT_DEPOSIT_SUCCESS_MESSAGE = "THE ACCOUNT HAVE BEEN CREDITED SUCCESSFULLY";

    public static final String INSUFFICIENT_FUNDS_CODE = "006";
    public static final String INSUFFICIENT_FUNDS_MESSAGE= "INSUFFICIENT FUNDS";

    public static final String ACCOUNT_WITHDRAW_SUCCESS_CODE = "007";
    public static final String ACCOUNT_WITHDRAW_SUCCESS_MESSAGE = "THE ACCOUNT HAVE BEEN DEBITED SUCCESSFULLY";

    public static final String ACCOUNT_TRANSFER_SUCCESS_CODE = "008";
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "THE TRANSFER HAVE BEEN COMPLETED SUCCESSFULLY";

    public static final String DEBIT_ACCOUNT_NOT_EXIST_CODE = "009";
    public static final String DEBIT_ACCOUNT_NOT_EXIST_MESSAGE = "DEBIT ACCOUNT NUMBER DOESNT EXIST";

    public static final String CREDIT_ACCOUNT_NOT_EXIST_CODE = "0010";
    public static final String CREDIT_ACCOUNT_NOT_EXIST_MESSAGE = "CREDIT ACCOUNT NUMBER DOESNT EXIST";

    public static String generateAccountNumber(){
        /**
         * Current Year + Random Six Digits
         */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // Generate random number between min and max
        int randomNumber =  min + (int) (Math.random() * (( max - min ) + 1));

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(currentYear)
                .append(randomNumber).toString();
    }
}
