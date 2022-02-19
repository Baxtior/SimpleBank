package banking.entity;

import banking.service.MathFunctions;
import banking.service.UniqueRandomNumber;

public class Card {

    public String generateValidCard() {
        String numberforLuhn = Bank.BIN + UniqueRandomNumber.getUniqueNumberWithLength(9);
        int luhn = MathFunctions.luhn(numberforLuhn);
        int last = 0;
        int n = luhn + last;
        while (n % 10 != 0) {
            last++;
            n = luhn + last;
        }
        return numberforLuhn + last;
    }
}
