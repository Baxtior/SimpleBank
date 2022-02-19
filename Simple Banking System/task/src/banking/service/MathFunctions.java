package banking.service;

public class MathFunctions {

    public static boolean passesLuhn(String number) {
        String cardNumber = number.substring(0, number.length() - 1);
        int last = Integer.parseInt(number.substring(number.length() - 1));
        int luhn = luhn(cardNumber);
        return  (luhn + last) % 10 == 0;
    }

    public static int luhn(String number) {
        String[] numbers = number.split("");
        return finalStep(secondStep(numbers, firstStep(numbers)));
    }

    private static int[] firstStep(String[] numbers) {
        int[] firstStep = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            if (i % 2 == 0) {
                firstStep[i] = Integer.parseInt(numbers[i]) * 2;
            } else {
                firstStep[i] = Integer.parseInt(numbers[i]);
            }
        }
        return firstStep;
    }

    private static int[] secondStep(String[] numbers, int[] firstStep) {
        int[] secondStep = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            if (firstStep[i] > 9) {
                secondStep[i] = firstStep[i] - 9;
            } else {
                secondStep[i] = firstStep[i];
            }
        }
        return secondStep;
    }

    private static int finalStep(int[] secondStep) {
        int sum = 0;
        for (int i : secondStep) {
            sum += i;
        }
        return sum;
    }
}
