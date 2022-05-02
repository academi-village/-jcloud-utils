package com.github.academivillage.jcloud.util.dynamikax.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * A utility class to encode/decode numbers to a compact alphabetic representation.
 *
 * @author Younes Rahimi
 */
public final class AlphabetEncoder {
    public static final char[] ALPHABET          = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final char[] EXTENDED_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzœ∑´†¥¨ˆπ“‘åß∂ƒ©˙∆˚¬…Ω≈ç√∫˜≤≥µ¡™£¢∞§¶•ªº≠`!@#$%^&*+'?<>:".toCharArray();

    static {
        Arrays.sort(ALPHABET);
        Arrays.sort(EXTENDED_ALPHABET);
    }

    private final char[] alphabet;
    private final int    encodeLength;

    public AlphabetEncoder() {
        this(EXTENDED_ALPHABET);
    }

    public AlphabetEncoder(char[] alphabet) {
        this.alphabet     = alphabet;
        this.encodeLength = alphabet.length;
    }

    /**
     * Encodes a long number to an alphabetic representation to compact it.
     *
     * @param victim The long number to encode.
     * @return The encoded representation of the long number.
     */
    public String encode(long victim) {
        final List<Character> list = new ArrayList<>();

        do {
            list.add(alphabet[(int) (victim % encodeLength)]);
            victim /= encodeLength;
        } while (victim > 0);

        Collections.reverse(list);
        return list.stream().map(Object::toString).collect(joining(""));
    }

    /**
     * Decodes an alphabetic number to the original long number.
     *
     * @param encoded The encoded number.
     * @return The decoded long number.
     */
    public long decode(final String encoded) {
        long ret = 0;
        char c;
        for (int index = 0; index < encoded.length(); index++) {
            c = encoded.charAt(index);
            ret *= encodeLength;
            ret += Arrays.binarySearch(EXTENDED_ALPHABET, c);
        }
        return ret;
    }
}
