package com.steve_md.smartmkulima.utils.services;

import android.text.TextUtils;
import android.util.Base64;

import java.text.DecimalFormat;

public class NumberUtil {

    /**
     * Round off to 2dp
     * and add comma separator.
     *
     * @param wholeNumber the whole number
     * @return string string
     */
    public static String formatNumber(String wholeNumber) {

        Double number = Double.parseDouble(wholeNumber);

        //NumberFormat.getInstance().format(myNumber);

        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        //NumberFormat formatter =NumberFormat.getInstance();
        final String formattedNumber = formatter.format(number);//add commas

        return formattedNumber;
    }

    /**
     * This method takes an account number and obscures the number leaving only
     * four trailing/last digits
     *
     * @param accountNo an account number that needs to be obscured so as to be displayed safely
     * @return the accountNo padded with *'s to reveal only the last four digits
     */
    public static final String displayAccountNumber(String accountNo) {
        return displayAccountNumber(accountNo, 4);
    }

    /**
     * This method takes an account number and obscures the number leaving only
     * a few trailing/last digits
     *
     * @param accountNo     an account number that needs to be obscured so as to be displayed safely
     * @param revealedChars specifies how many trailing digits of the account number to reveal
     * @return the accountNo padded with *'s to reveal only the last four digits
     */
    public static final String displayAccountNumber(String accountNo, int revealedChars) {
        if (TextUtils.isEmpty(accountNo)) {
            return "";
        } else if (accountNo.length() <= revealedChars) {
            return accountNo;
        }
        String leadingChars = TextUtils.substring(accountNo, 0,
                accountNo.length() - revealedChars);
        String replacementChars = "";
        int len = leadingChars.length();
        for (int i = 0; i < len; i++) {
            replacementChars += "*";
        }
        accountNo = accountNo.replace(leadingChars, replacementChars);
        return accountNo;
    }


    /**
     * Mobile number string.
     *
     * @param mobileNumber the mobile number
     * @return the string
     */
    public static String mobileNumber(String mobileNumber) {

        if (mobileNumber.startsWith("0")) {
            return mobileNumber.substring(1, mobileNumber.length()).replaceAll("\\W", "");

        }

        return mobileNumber;
    }

    /**
     * Hex string to byte array byte [ ].
     *
     * @param s the s
     * @return the byte [ ]
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] decodeData(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }
}

