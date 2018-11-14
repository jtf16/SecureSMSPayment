package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    private static final int DEFAULT_AMOUNT_SIZE = 8;

    private static final HashMap<String, Integer> stringDecimalMap = new HashMap<String, Integer>() {{
        int i = 0;
        for (char ch = '0'; ch <= '9'; ++ch)
            put(String.valueOf(ch), i++);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            put(String.valueOf(ch), i++);
        for (char ch = 'A'; ch <= 'Z' || i < 64; ++ch)
            put(String.valueOf(ch), i++);
    }};

    private static final HashMap<Integer, String> decimalStringMap = new HashMap<Integer, String>() {{
        int i = 0;
        for (char ch = '0'; ch <= '9'; ++ch)
            put(i++, String.valueOf(ch));
        for (char ch = 'a'; ch <= 'z'; ++ch)
            put(i++, String.valueOf(ch));
        for (char ch = 'A'; ch <= 'Z' || i < 64; ++ch)
            put(i++, String.valueOf(ch));
    }};

    public static String parseIbanAndAmount(String iban, String amount) {
        iban = StringUtils.deleteWhitespace(iban);
        Float fAmount = Float.valueOf(amount) * 100;
        amount = correctSize(String.valueOf(fAmount.intValue()), DEFAULT_AMOUNT_SIZE);
        return parseMessage(iban + amount);
    }

    public static String parseMessage(String message) {
        message = originalStringToBinary(message);
        return binaryToString(message);
    }

    public static String unparseMessage(List lettersPos, String message) {
        lettersPos = lettersPos == null ? new ArrayList() : lettersPos;
        message = stringToBinary(message);
        return binaryToOriginalString(lettersPos, message);
    }

    public static int getCode(String message) {

        message = unparseMessage(null, message);

        return Integer.parseInt(message.substring(0, 1));
    }

    public static int getBalance(String message) {

        message = unparseMessage(null, message);

        message = new StringBuffer(
                message.substring(1, message.length())).reverse().toString();
        return Integer.parseInt(message);
    }

    public static float getFloatBalance(String message) {
        return (float) getBalance(message) / 100;
    }

    public static String correctSize(String val, int size) {
        while (val.length() < size) {
            val = "0" + val;
        }
        return val.substring(val.length() - size);
    }

    public static String originalStringToBinary(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            result = result.concat(correctSize(Integer.toBinaryString(
                    stringDecimalMap.get(String.valueOf(c))), Character.isLetter(c) ? 5 : 4));
        }
        while (result.length() % 6 != 0) {
            result += "0";
        }
        return result;
    }

    public static String stringToBinary(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            result = result.concat(correctSize(Integer.toBinaryString(
                    stringDecimalMap.get(String.valueOf(c))), 6));
        }
        return result;
    }

    public static String binaryToString(String s) {
        List<String> sList = splitAfterNChars(s, 6);
        String result = "";
        for (String s1 : sList) {
            int a = Integer.parseInt(s1, 2);
            result = result.concat(decimalStringMap.get(a));
        }
        return result;
    }

    public static String binaryToOriginalString(List lettersPos, String s) {
        List<String> list = new ArrayList<>();
        int j = 0;
        for (int i = 0; i + 4 <= s.length(); i += 4) {
            if (lettersPos.contains(j)) {
                list.add("1" + s.substring(i, i + 5));
                i++;
            } else {
                list.add(s.substring(i, i + 4));
            }
            j++;
        }
        String result = "";
        for (String s1 : list) {
            int a = Integer.parseInt(s1, 2);
            result = result.concat(decimalStringMap.get(a));
        }
        return result;
    }

    public static List<String> splitAfterNChars(String input, int splitLen) {
        List<String> parts = new ArrayList<>();
        int len = input.length();
        for (int i = 0; i < len; i += splitLen) {
            parts.add(input.substring(i, Math.min(len, i + splitLen)));
        }
        return parts;
    }
}
