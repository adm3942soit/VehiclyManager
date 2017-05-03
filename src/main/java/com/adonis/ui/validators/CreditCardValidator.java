package com.adonis.ui.validators;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

/**
 * Created by oksdud on 03.05.2017.
 */
public class CreditCardValidator {

        public static boolean isValidString(String value) {
            return checkCardValidity(value);
        }

        private static String getDigitsOnly(String s) {
            StringBuilder stringToCheck = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
                char c = s.charAt(i);
                if(Character.isDigit(c)) {
                    stringToCheck.append(c);
                }
            }

            return stringToCheck.toString();
        }

        private static boolean checkCardValidity(String cardNumber) {
            String stringToCheck = getDigitsOnly(cardNumber);
            if(stringToCheck.isEmpty()) {
                return false;
            } else {
                int sum = 0;
                boolean timesTwo = false;

                int modulus;
                for(modulus = stringToCheck.length() - 1; modulus >= 0; --modulus) {
                    int digit = Integer.parseInt(stringToCheck.substring(modulus, modulus + 1));
                    int addend;
                    if(timesTwo) {
                        addend = digit * 2;
                        if(addend > 9) {
                            addend -= 9;
                        }
                    } else {
                        addend = digit;
                    }

                    sum += addend;
                    timesTwo = !timesTwo;
                }

                modulus = sum % 10;
                return modulus == 0;
            }
        }


}
