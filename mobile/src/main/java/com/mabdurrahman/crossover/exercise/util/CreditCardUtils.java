/*
 * Copyright (c) Mahmoud Abdurrahman 2017. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mabdurrahman.crossover.exercise.util;

import java.util.regex.Pattern;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 * A utility for validating and formatting credit card numbers
 */
public class CreditCardUtils {

    private static final int CARD_LENGTH_FOR_TYPE = 4;

    private static final Pattern REGX_VISA = Pattern.compile("^4[0-9]{6,}$");
    private static final Pattern REGX_MASTERCARD = Pattern.compile("^5[1-5][0-9]{5,}$");

    private static final Pattern TYPE_VISA = Pattern.compile("^4[0-9]{3}?");
    private static final Pattern TYPE_MASTERCARD = Pattern.compile("^5[1-5][0-9]{2}$");

    /**
     * @param card the input to validate
     * @return true if card is a valid card for this instance's card types
     */
    public static boolean validateCard(CharSequence card) {
        return validateCard(card, findCardType(card));
    }

    /**
     * @param card the input to validate
     * @param creditCardType the card type to validate against
     * @return true if card is a valid card for creditCard
     */
    public static boolean validateCard(CharSequence card, CreditCardType creditCardType) {
        return creditCardType.getVerifyPattern().matcher(card).matches();
    }

    /**
     * @param s the card number to find type of, can be partial
     * @return the instance of CreditCard matching s, or null if no match
     */
    public static CreditCardType findCardType(CharSequence s) {
        String cardNumber = clean(s);
        if (cardNumber.length() >= CARD_LENGTH_FOR_TYPE) {
            for (CreditCardType card : CardType.values()) {
                if (card.getTypePattern()
                        .matcher(cardNumber.subSequence(0, CARD_LENGTH_FOR_TYPE))
                        .matches()) {
                    return card;
                }
            }
        }

        return null;
    }

    /**
     * @param cardNumber the input to clean
     * @return a string containing only digits
     */
    public static String clean(CharSequence cardNumber) {
        return cardNumber.toString().trim().replaceAll("[^\\d]", "");
    }

    /**
     * Model for identifying, validating, and formatting credit cards
     */
    public interface CreditCardType {

        /**
         * @return a pattern which will be used to identify as this type using the first 4 digits
         */
        Pattern getTypePattern();

        /**
         * @return a pattern that can match non-formatted card numbers for this type
         */
        Pattern getVerifyPattern();

        /**
         * @return an array of grouping lengths. I.E. for VISA its { 4, 4, 4, 4 }
         */
        int[] getFormat();
    }

    /**
     * Simple implementation for {@linkplain CreditCardType}
     */
    protected enum CardType implements CreditCardType {
        VISA(REGX_VISA, TYPE_VISA, new int[] { 4, 4, 4, 4 }),
        MASTERCARD(REGX_MASTERCARD, TYPE_MASTERCARD, new int[] { 4, 4, 4, 4 });

        final Pattern typePattern;
        final Pattern verifyPattern;
        final int[] format;

        CardType(Pattern verifyPattern, Pattern typePattern, int[] format) {
            this.verifyPattern = verifyPattern;
            this.typePattern = typePattern;
            this.format = format;
        }

        @Override
        public int[] getFormat() {
            return format;
        }

        @Override
        public Pattern getTypePattern() {
            return typePattern;
        }

        @Override
        public Pattern getVerifyPattern() {
            return verifyPattern;
        }
    }

}
