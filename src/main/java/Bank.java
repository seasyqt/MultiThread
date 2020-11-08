import java.util.HashMap;
import java.util.Random;

public class Bank {

  private HashMap<String, Account> accounts;
  private final Random random = new Random();

  public Bank() {
    this.accounts = new HashMap<>();
  }

  public void addAccount(String accountNum, Account account) {
    this.accounts.put(accountNum, account);
  }

  public HashMap<String, Account> getAccounts() {
    return accounts;
  }

  public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
      throws InterruptedException {
    Thread.sleep(1000);
    return random.nextBoolean();
  }

  /**
   * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
   * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
   * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
   * усмотрение)
   */
  public void transfer(String fromAccountNum, String toAccountNum, long amount)
      throws InterruptedException {
    Account from = this.accounts.get(fromAccountNum);
    Account to = this.accounts.get(toAccountNum);
    if (!from.isBlock() && !to.isBlock()) {
      if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
        from.blockAccount();
        to.blockAccount();
      } else {
        synchronized (from.compareTo(to) > 0 ? from : to) {
          from.transferTo(to, amount);
        }
      }
    }
  }

  /**
   * TODO: реализовать метод. Возвращает остаток на счёте.
   */
  public long getBalance(String accountNum) {
    return this.accounts
        .get(accountNum)
        .getMoney();
  }

  public long allBalanceBank() {
    return this.accounts
        .values()
        .parallelStream()
        .mapToLong(Account::getMoney).sum();
  }
}
