import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  static Bank bank = new Bank();
  static AtomicInteger count = new AtomicInteger();

  public static void main(String[] args)  {

    HashSet<Thread> set = new HashSet<>();

    System.out.println("Сумма в банке -> ДО " + bank.allBalanceBank());

    for (int i = 0; i < 10; i++) {
      set.add(new Thread(Main::trans));
    }
    set.forEach(k -> k.start());
    set.forEach(k -> {
      try {
        k.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    System.out.println("Кол-во транзакций - " + count.get());
    System.out.println("Сумма в банке -> ПОСЛЕ " + bank.allBalanceBank());

  }

  private static void trans() {
    for (int i = 0; i < 100000; i++) {
      String account1 = Integer.toString((int) (Math.random() * 5) + 1);
      String account2 = Integer.toString((int) (Math.random() * 5) + 6);
      long amount = (long) (Math.random() * 100 + 100);
      try {
        bank.transfer(account1, account2, amount);
        count.incrementAndGet();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
