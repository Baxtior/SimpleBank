package banking.service;

import java.util.*;

public class UniqueRandomNumber {
    private static final Set<String> numbers = new HashSet<>();
    private static final Set<String> pins = new HashSet<>();

    private static String generateNumber(int length) {
        String number = shuffle(length);
        boolean isUnique = numbers.add(number);
        while (!isUnique) {
            number = shuffle(length);
            isUnique = numbers.add(number);
        }
        return number;
    }

    public static int size() {
        return numbers.size();
    }

    public static String getUniqueNumberWithLength(int length) {
        return generateNumber(length);
    }

    public static String getUniquePinWithLength(int length) {
        generatePin(length);
        return pins.iterator().next();
    }

    private static void generatePin(int length) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        StringBuilder builder = new StringBuilder();
        for (int i : list) {
            builder.append(i);
        }
        builder.deleteCharAt(length);
        pins.add(builder.toString());
    }

    private static String shuffle(int length) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        StringBuilder builder = new StringBuilder();
        for (int i : list) {
            builder.append(i);
        }
        builder.deleteCharAt(length);
        return builder.toString();
    }
}
