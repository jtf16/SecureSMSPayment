package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    public static final HashMap<String, Integer> stringDecimalMap = new HashMap<String, Integer>() {{
        int i = 0;
        for (char ch = '0'; ch <= '9'; ++ch)
            put(String.valueOf(ch), i++);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            put(String.valueOf(ch), i++);
        for (char ch = 'A'; ch <= 'Z' || i < 64; ++ch)
            put(String.valueOf(ch), i++);
    }};

    public static final HashMap<Integer, String> decimalStringMap = new HashMap<Integer, String>() {{
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
        amount = String.valueOf(Float.valueOf(amount) * 100);
        amount = correctSize(amount.replace(".", ""), 8);
        String binary = originalStringToBinary(iban + amount);
        return binaryToString(binary);
    }

    public static String correctSize(String val, int size) {
        while (val.length() % size != 0) {
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

    public static String binaryToOriginalString(String s) {
        List<String> list = new ArrayList<>();
        list.add("1" + s.substring(0, 5));
        list.add("1" + s.substring(5, 10));
        for (int i = 10; i + 4 <= s.length(); i += 4) {
            list.add(s.substring(i, i + 4));
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
