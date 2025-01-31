package com.teamproject.back.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;

@Component
public class RandomUtil {

    public static String getRandom(int size) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        return random.ints(size, 0, characters.length())
                .mapToObj(i -> String.valueOf(characters.charAt(i)))
                .collect(Collectors.joining());
    }
}
