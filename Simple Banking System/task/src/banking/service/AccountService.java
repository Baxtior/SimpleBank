package banking.service;

import banking.datasource.DataSource;
import banking.entity.Account;
import banking.entity.Card;

public class AccountService {
    private static DataSource dataSource;
    private final Card card = new Card();

    public void connectDataSource(String fileName){
        dataSource = new DataSource(fileName);
    }

    public Account createAccount() {
        dataSource.createNewTable();
        Account account = new Account();
        account.setNumber(card.generateValidCard());
        account.setPin(UniqueRandomNumber.getUniquePinWithLength(4));
        account.setBalance(0);
        dataSource.insert(account.getNumber(), account.getPin(), account.getBalance());
        return account;
    }

    public Account deleteAccount(Integer id) {
        Account account = new Account();
        account.setId(id);
        account.setNumber(dataSource.getAccountNumber(id));
        account.setPin(dataSource.getAccountPin(id));
        account.setBalance(dataSource.getAccountBalance(id));
        dataSource.closeAccount(id);
        return account;
    }

    public boolean isSameAccount(Account account, String number) {
        Account acc = new Account();
        acc.setId(dataSource.getAccountId(number));
        return account.getId() == acc.getId();
    }

    public Account addIncome(Integer id, int amount) {
        Account account = new Account();
        account.setId(id);
        account.setNumber(dataSource.getAccountNumber(id));
        account.setPin(dataSource.getAccountPin(id));
        dataSource.addIncome(id, amount);
        account.setBalance(dataSource.getAccountBalance(id));
        return account;
    }

    public Account transfer(Integer fromId, Integer toId, int amount) {
        Account account = new Account();
        account.setId(fromId);
        account.setNumber(dataSource.getAccountNumber(fromId));
        account.setPin(dataSource.getAccountPin(fromId));
        dataSource.transfer(fromId, toId, amount);
        account.setBalance(dataSource.getAccountBalance(fromId));
        return account;
    }

    public Account getAccount(String number, String pin) {
        if (hasSuchAccount(number, pin)) {
            Account account = new Account();
            account.setId(dataSource.getCardId(number, pin));
            account.setNumber(number);
            account.setPin(pin);
            account.setBalance(dataSource.getAccountBalance(account.getId()));
            return account;
        }
        return null;
    }

    public boolean hasSuchAccount(String number, String pin) {
        return dataSource.getCard(number, pin);
    }

    public boolean containsNumber(String number) {
        return dataSource.containsCard(number);
    }

    public boolean hasEnoughMoney(Integer id, int amount) {
        return dataSource.hasEnoughMoney(id, amount);
    }

    public Account getAccountFromNumber(String cardNumber) {
        int accountId = dataSource.getAccountId(cardNumber);
        Account account = new Account();
        account.setId(accountId);
        account.setNumber(dataSource.getAccountNumber(accountId));
        account.setPin(dataSource.getAccountPin(accountId));
        account.setBalance(dataSource.getAccountBalance(accountId));
        return account;
    }

}
