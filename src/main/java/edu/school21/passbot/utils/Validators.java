package edu.school21.passbot.utils;

import java.time.LocalDate;

public class Validators {
    public static boolean isCorrectName(String s) {
        return s.chars().allMatch(Character::isLetter);
    }

    public static boolean isCorrectDate(String s) {
        LocalDate date = ParseUtils.parseDate(s);
        return date != null;
    }
}
