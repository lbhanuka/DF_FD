package com.epic.common.util;

import java.math.BigDecimal;

public class NumberWordConverter  {

    public static final String INVALID_INPUT_GIVEN = "Invalid input given";

    public static final String[] ones = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };

    public static final String[] tens = {
            "", // 0
            "", // 1
            "Twenty", // 2
            "Thirty", // 3
            "Forty", // 4
            "Fifty", // 5
            "Sixty", // 6
            "Seventy", // 7
            "Eighty", // 8
            "Ninety" // 9
    };

    /*public static String getMoneyIntoWords(String input) {
        MoneyConverters converter = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;
        return converter.asWords(new BigDecimal(input));
    }*/

    public static String getMoneyIntoWords(final double money) {
        long dollar = (long) money;
        long cents = Math.round((money - dollar) * 100);
        if (money == 0D) {
            return "";
        }
        if (money < 0) {
            return INVALID_INPUT_GIVEN;
        }
        String dollarPart = "";
        if (dollar > 0) {
            dollarPart = "Rupee" + (dollar == 1 ? " " : "s " + convert(dollar));
        }
        String centsPart = "";
        if (cents > 0) {
            if (dollarPart.length() > 0) {
                centsPart = " And ";
            }
            centsPart += convert(cents) + " Cent" + (cents == 1 ? "" : "s");
        }
        return dollarPart + centsPart + " Only";
    }

    private static String convert(final long n) {
        if (n < 0) {
            return INVALID_INPUT_GIVEN;
        }
        if (n < 20) {
            return ones[(int) n];
        }
        if (n < 100) {
            return tens[(int) n / 10] + ((n % 10 != 0) ? " " : "") + ones[(int) n % 10];
        }
        if (n < 1000) {
            return ones[(int) n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }
        if (n < 1_000_000) {
            return convert(n / 1000) + " Thousand" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
        }
        if (n < 1_000_000_000) {
            return convert(n / 1_000_000) + " Million" + ((n % 1_000_000 != 0) ? " " : "") + convert(n % 1_000_000);
        }
        return convert(n / 1_000_000_000) + " Billion" + ((n % 1_000_000_000 != 0) ? " " : "") + convert(n % 1_000_000_000);
    }
}
