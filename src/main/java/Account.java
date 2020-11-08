import java.util.concurrent.atomic.AtomicLong;

public class Account implements Comparable<Account> {

  private AtomicLong money = new AtomicLong();
  private String accNumber;
  private boolean isBlock;

  public Account(String accNumber) {
    this.accNumber = accNumber;
  }

  public Account(long money, String accNumber) {
    this.money.set(money);
    this.accNumber = accNumber;
    this.isBlock = false;
  }

  public long getMoney() {
    return money.get();
  }

  public void setMoney(long money) {
    this.money.set(money);
  }

  public String getAccNumber() {
    return accNumber;
  }

  public void setAccNumber(String accNumber) {
    this.accNumber = accNumber;
  }

  public boolean isBlock() {
    return isBlock;
  }

  public synchronized void refill(long money) {
    if (!isBlock) {
      long newBalance = this.getMoney() + money;
      this.setMoney(newBalance);
    } else {
      System.out.println("Счет заблокирован");
    }
  }

  public synchronized void withdrawal(long money) throws InterruptedException {
    if (!isBlock) {
      long newBalance = this.getMoney() - money;
      this.setMoney(newBalance);
    } else {
      System.out.println("Счет заблокирован");
    }
  }

  public void transferTo(Account account, long money) throws InterruptedException {
    long isPositiveMoneyAccount = this.getMoney() - money;
    if (isNotBlockedAccounts(account) && isPositiveMoneyAccount >= 0) {
      withdrawal(money);
      account.refill(money);
    }
  }

  public void blockAccount() {
    this.isBlock = true;
  }

  private boolean isNotBlockedAccounts(Account account) {
    return !this.isBlock() && !account.isBlock();
  }

  @Override
  public int compareTo(Account account) {
    return this.getAccNumber().compareTo(account.getAccNumber());
  }
}
