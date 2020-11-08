import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import junit.framework.TestCase;
import org.junit.Test;


public class MultiThreadTransferTest extends TestCase {

  Bank bank;
  Random random;
  int numOfAccount = 10;
  final int threadCount = 10;
  final int transfersPerThread = 1000;
  ArrayList<Account> accounts = new ArrayList<>();
  ArrayList<Thread> threads = new ArrayList<>();

  @Override
  protected void setUp() throws Exception {
    bank = new Bank();
    random = new Random();
    for (int i = 0; i < numOfAccount; i++) {
      String accNumber = String.valueOf(i + 31);
      Account account = new Account(accNumber);
      account.setMoney(1000000);
      accounts.add(account);
      bank.addAccount(accNumber, account);
    }
  }

  @Test
  public void testBank() throws InterruptedException {
    long sumOfMoney = bank.getAccounts().values().stream().mapToLong(Account::getMoney).sum();
    for (int i = 0; i < threadCount; i++) {
      threads.add(new Thread(() -> {
        for (int j = 0; j < transfersPerThread; j++) {
          Account from = accounts.get(random.nextInt(accounts.size()));
          Account to = accounts.get(random.nextInt(accounts.size()));
          int sum = random.nextInt(50100);
          try {
            bank.transfer(from.getAccNumber(), to.getAccNumber(), sum);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }));
    }
    threads.forEach(Thread::start);
    for (Thread thread : threads) {
      thread.join();
    }

    long result = bank.getAccounts().values().stream().mapToLong(Account::getMoney).sum();
    System.out.println(sumOfMoney + "\n" + result);
    System.out.println(
        accounts.stream().map(account -> account.getAccNumber() + "-" + account.isBlock()).collect(
            Collectors.toList()));
    assertEquals(sumOfMoney, result);
  }
}